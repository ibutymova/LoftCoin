package com.example.butymovaloftcoin.screens.main.rate;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.ShareCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.butymovaloftcoin.R;
import com.example.butymovaloftcoin.data.db.model.CoinEntity;
import com.example.butymovaloftcoin.data.model.Fiat;
import com.example.butymovaloftcoin.data.prefs.Prefs;
import com.example.butymovaloftcoin.job.JobHelper;
import com.example.butymovaloftcoin.job.JobHelperImpl;
import com.example.butymovaloftcoin.job.SyncRateJobService;
import com.example.butymovaloftcoin.screens.main.rate.dialogs.AppSettingsDialog;
import com.example.butymovaloftcoin.screens.main.rate.dialogs.CurrencyDialog;
import com.example.butymovaloftcoin.screens.main.rate.dialogs.PermissionDialog;
import com.example.butymovaloftcoin.screens.main.rate.dialogs.RateNotifyOffDialog;
import com.example.butymovaloftcoin.screens.main.rate.dialogs.RateNotifyOnDialog;
import com.example.butymovaloftcoin.utils.CurrencyFormatter;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPHeaderCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.PublishSubject;

import static android.os.Environment.DIRECTORY_DOCUMENTS;
import static androidx.core.app.NotificationManagerCompat.IMPORTANCE_NONE;
import static com.example.butymovaloftcoin.screens.main.rate.dialogs.CurrencyDialog.EXTRA_CURRENCY;
import static com.example.butymovaloftcoin.screens.main.rate.dialogs.RateNotifyOnDialog.KEY_RATE_SYMBOL;

public class RateRouterImpl implements RateRouter,
                                       JobHelper.Listener {

    private static final int RC_PERMISSION = 105;
    private static final int RC_PERMISSION_WITH_RATIONALE = 106;
    private static final int RC_CURRENCY_DIALOG = 107;
    private static final int RC_APP_SETTINGS_PERMISSIONS_DIALOG = 108;
    private static final int RC_APP_SETTINGS_NOTIFICATION_DIALOG = 109;
    private static final int RC_PERMISSION_DIALOG = 110;
    private static final int RC_RATE_NOTIFY_OFF_DIALOG = 111;
    private static final int RC_RATE_NOTIFY_ON_DIALOG = 112;
    private static final float PDF_TITLE_FONT_SIZE = 26.0f;
    private static final float PDF_HEADER_FONT_SIZE = 16.0f;
    private static final float PDF_FONT_SIZE = 14.0f;

    private PublishSubject<Fiat> onCurrencySelectedEvent = PublishSubject.create();
    private PublishSubject<String> onJobStartedEvent = PublishSubject.create();
    private PublishSubject<Object> onJobStoppedEvent = PublishSubject.create();
    private PublishSubject<Boolean> getNotificationStateEvent = PublishSubject.create();
    private PublishSubject<String> onConfirmationRateNotifyOnEvent = PublishSubject.create();
    private PublishSubject<Object> onConfirmationRateNotifyOffEvent = PublishSubject.create();
    private PublishSubject<Object> onRequestPermissionTrueEvent = PublishSubject.create();
    private PublishSubject<Integer> onRequestPermissionFalseEvent = PublishSubject.create();
    private PublishSubject<Integer> onCreatePdfFalseEvent = PublishSubject.create();
    private PublishSubject<Integer> onRequestNotificationFalseEvent = PublishSubject.create();

    private Fragment fragment;
    private SimpleDateFormat simpleDateFormat;
    private CurrencyFormatter currencyFormatter;
    private Prefs prefs;
    private CompositeDisposable compositeDisposable;

    @Inject
    public RateRouterImpl(SimpleDateFormat simpleDateFormat, CurrencyFormatter currencyFormatter, Prefs prefs, CompositeDisposable compositeDisposable) {
        this.simpleDateFormat = simpleDateFormat;
        this.currencyFormatter = currencyFormatter;
        this.prefs = prefs;
        this.compositeDisposable = compositeDisposable;
    }

    @Override
    public void attach(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void showCurrencyDialog() {
        CurrencyDialog currencyDialog = new CurrencyDialog();
        currencyDialog.setTargetFragment(fragment, RC_CURRENCY_DIALOG);
        currencyDialog.show(fragment.requireActivity().getSupportFragmentManager(), CurrencyDialog.TAG);
    }

    @Override
    public void showRateNotifyOnDialog(String symbol) {
        RateNotifyOnDialog rateNotifyOnDialog = new RateNotifyOnDialog();
        rateNotifyOnDialog.setTargetFragment(fragment, RC_RATE_NOTIFY_ON_DIALOG);
        rateNotifyOnDialog.setSymbol(symbol);
        rateNotifyOnDialog.show(fragment.requireActivity().getSupportFragmentManager(), RateNotifyOnDialog.TAG);
    }

    @Override
    public void startJob(String symbol) {
        JobHelper jobHelper = new JobHelperImpl(fragment.requireActivity().getApplicationContext(), this);
        jobHelper.startSyncRateJob(symbol);
    }

    @Override
    public void stopJob() {
        JobHelper jobHelper = new JobHelperImpl(fragment.requireActivity().getApplicationContext(), this);
        jobHelper.stopSyncRateJob();
    }

    @Override
    public void showRateNotifyOffDialog() {
        RateNotifyOffDialog rateNotifyOffDialog = new RateNotifyOffDialog();
        rateNotifyOffDialog.setTargetFragment(fragment, RC_RATE_NOTIFY_OFF_DIALOG);
        rateNotifyOffDialog.show(fragment.requireActivity().getSupportFragmentManager(), RateNotifyOffDialog.TAG);
    }

    private String getPath(){
        File dir = new File(fragment.requireActivity().getString(R.string.pdf_format_file_dir, fragment.requireActivity().getExternalFilesDir(null),
                File.separator,
                fragment.requireActivity().getString(R.string.app_name)),
                File.separator);
        if (!dir.exists())
            dir.mkdir();
        return fragment.requireActivity().getString(R.string.pdf_format_file_name, dir.getPath());
    }

    private void createPdf(String path, List<CoinEntity> coinEntities){
        compositeDisposable.add(Observable.fromCallable(() -> {
            File file = new File(path);
            Document document = new Document();

            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();
            document.setPageSize(PageSize.A4);
            document.addCreationDate();

            BaseFont baseFont = BaseFont.createFont( "assets/fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font titleFont = new Font(baseFont, PDF_TITLE_FONT_SIZE, Font.NORMAL, BaseColor.BLACK);
            Font headerFont = new Font(baseFont, PDF_HEADER_FONT_SIZE, Font.NORMAL, BaseColor.BLACK);

            addTitle(document, titleFont);
            PdfPTable table = new PdfPTable(3);
            addHeaders(table, headerFont);
            addCells(table, baseFont, coinEntities);

            document.add(table);
            document.close();
            return file;
        }).subscribe(file -> {
            Uri uri = FileProvider.getUriForFile(fragment.requireActivity(), fragment.requireActivity().getPackageName(), file);

            Intent shareIntent = ShareCompat.IntentBuilder.from(fragment.requireActivity())
                    .setSubject(fragment.requireActivity().getString(R.string.mail_subject, simpleDateFormat.format(System.currentTimeMillis())))
                    .setType(fragment.requireActivity().getContentResolver().getType(uri))
                    .setStream(uri)
                    .createChooserIntent()
                    .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION|
                              Intent.FLAG_ACTIVITY_NEW_TASK|
                              Intent.FLAG_ACTIVITY_NO_HISTORY|
                              Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            fragment.startActivity(shareIntent);

        }, throwable -> onCreatePdfFalseEvent.onNext(R.string.error_create_pdf)));
    }

    private void addHeader(PdfPTable table, String text, Font font, int align){
        PdfPHeaderCell cell = new PdfPHeaderCell();
        cell.setPhrase(new Phrase(text, font));
        setCellSettings(cell, align);
        table.addCell(cell);
    }

    private void addHeaders(PdfPTable table, Font font){
        addHeader(table, fragment.requireActivity().getString(R.string.pdf_column1_title), font, Element.ALIGN_LEFT);
        addHeader(table, fragment.requireActivity().getString(R.string.pdf_column2_title, prefs.getFiatCurrency().symbol), font, Element.ALIGN_LEFT);
        addHeader(table, fragment.requireActivity().getString(R.string.pdf_column3_title), font, Element.ALIGN_RIGHT);
    }

    private void addCells(PdfPTable table, BaseFont baseFont, List<CoinEntity> coinEntities){
        Font font = new Font(baseFont, PDF_FONT_SIZE, Font.NORMAL, BaseColor.BLACK);
        Font fontUp = new Font(baseFont, PDF_FONT_SIZE, Font.NORMAL,  new BaseColor(ContextCompat.getColor(fragment.requireActivity(), R.color.percent_change_up)));
        Font fontDown = new Font(baseFont, PDF_FONT_SIZE, Font.NORMAL, new BaseColor(ContextCompat.getColor(fragment.requireActivity(), R.color.percent_change_down)));

        for(CoinEntity coinEntity : coinEntities){
            addCell(table, coinEntity.symbol, Element.ALIGN_LEFT, font);
            addCell(table, currencyFormatter.format(coinEntity.getQuote(prefs.getFiatCurrency()).price, false), Element.ALIGN_LEFT, font);
            float percentChange24h = coinEntity.getQuote(prefs.getFiatCurrency()).percentChange24h;
            if (percentChange24h>0)
                addCell(table, fragment.requireActivity().getString(R.string.pdf_column3_format, percentChange24h), Element.ALIGN_RIGHT, fontUp);
            else
                addCell(table, fragment.requireActivity().getString(R.string.pdf_column3_format, percentChange24h), Element.ALIGN_RIGHT, fontDown);
        }
    }

    private void addCell(PdfPTable table, String text, int align, Font font){
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        setCellSettings(cell, align);
        table.addCell(cell);
    }

    private void addTitle(Document document, Font font) throws DocumentException {
        Paragraph paragraph = new Paragraph();
        paragraph.setSpacingAfter(20);
        paragraph.add(new Phrase(fragment.requireActivity().getString(R.string.pdf_title, simpleDateFormat.format(System.currentTimeMillis())), font));
        paragraph.setAlignment(Element.ALIGN_CENTER);
        document.add(paragraph);
    }

    private void setCellSettings(PdfPCell cell, int align){
        cell.setHorizontalAlignment(align);
        cell.setBorder(Rectangle.BOTTOM);
        cell.setBorderWidthBottom(1);
        cell.setPaddingRight(5);
        cell.setPaddingLeft(5);
        cell.setBorderColor(new BaseColor(R.color.blue_grey));
        cell.setFixedHeight(30);
    }

    @Override
    public void startRateShareActivity(List<CoinEntity> coinEntities){
        createPdf(getPath(), coinEntities);
    }

    @Override
    public void requestPermissionCheck() {
        int permissionStatus = ContextCompat.checkSelfPermission(fragment.requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionStatus == PackageManager.PERMISSION_GRANTED)
            onRequestPermissionTrueEvent.onNext(new Object());
        else
            requestPermissionWithRationale();
    }

    private void requestPermission(int requestCode){
        fragment.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,@NonNull int[] grantResults) {
        boolean allow;

        switch (requestCode){
            case RC_PERMISSION:
            case RC_PERMISSION_WITH_RATIONALE: {
                allow = (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED);
                break;
            }
            default:{
                allow = false;
                break;
            }
        }

        if (allow)
            onRequestPermissionTrueEvent.onNext(new Object());
        else {
            if (fragment.shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                onRequestPermissionFalseEvent.onNext(R.string.error_request_permission);
            else {
                if (requestCode != RC_PERMISSION)
                    showAppSettingsDialog(RC_APP_SETTINGS_PERMISSIONS_DIALOG, R.string.app_settings_permissions_message);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case RC_CURRENCY_DIALOG:{
                onCurrencySelectedEvent.onNext((Fiat)data.getSerializableExtra(EXTRA_CURRENCY));
                return;
            }
            case RC_APP_SETTINGS_PERMISSIONS_DIALOG:{
                if (resultCode == Activity.RESULT_OK)
                    showAppDetailSettings();
                else
                    onRequestPermissionFalseEvent.onNext(R.string.error_request_permission);
                return;
            }
            case RC_APP_SETTINGS_NOTIFICATION_DIALOG:{
                if (resultCode == Activity.RESULT_OK)
                    showAppDetailSettings();
                else
                    onRequestNotificationFalseEvent.onNext(R.string.error_request_notification);
                return;
            }
            case RC_RATE_NOTIFY_OFF_DIALOG: {
                onConfirmationRateNotifyOffEvent.onNext(new Object());
                return;
            }
            case RC_RATE_NOTIFY_ON_DIALOG: {
                onConfirmationRateNotifyOnEvent.onNext(data.getStringExtra(KEY_RATE_SYMBOL));
                return;
            }
            case RC_PERMISSION_DIALOG: {
                if (resultCode == Activity.RESULT_OK)
                    requestPermission(RC_PERMISSION);
                else
                    onRequestPermissionFalseEvent.onNext(R.string.error_request_permission);
                return;
            }
        }
    }

    @Override
    public void showError(Integer resId) {
        Toast.makeText(fragment.requireActivity().getApplicationContext(), fragment.getString(resId), Toast.LENGTH_LONG).show();
    }

    @Override
    public void showRateNotifyOnToast(String symbol) {
        Toast.makeText(fragment.requireActivity().getApplicationContext(), String.format(fragment.getString(R.string.rate_notify_on_result), symbol), Toast.LENGTH_LONG).show();
    }

    @Override
    public void showRateNotifyToast() {
        Toast.makeText(fragment.requireActivity().getApplicationContext(), fragment.getString(R.string.rate_notify_toast), Toast.LENGTH_LONG).show();
    }

    @Override
    public void showRateNotifyOffToast() {
        Toast.makeText(fragment.requireActivity().getApplicationContext(), fragment.getString(R.string.rate_notify_off_result), Toast.LENGTH_LONG).show();
    }

    private void requestPermissionWithRationale() {
        if (fragment.shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE))
            showPermissionDialog();
        else
            requestPermission(RC_PERMISSION_WITH_RATIONALE);
    }

    private void showPermissionDialog(){
        PermissionDialog dialog = new PermissionDialog();
        dialog.setTargetFragment(fragment, RC_PERMISSION_DIALOG);
        dialog.show(fragment.requireActivity().getSupportFragmentManager(), PermissionDialog.TAG);
    }

    private void showAppSettingsDialog(int requestCode, @StringRes int messageId) {
        AppSettingsDialog dialog = new AppSettingsDialog();
        dialog.setTargetFragment(fragment, requestCode);
        dialog.setMessageId(messageId);
        dialog.show(fragment.requireActivity().getSupportFragmentManager(), AppSettingsDialog.TAG);
    }

    @Override
    public void getNotificationState() {
        boolean areNotificationsEnabled = NotificationManagerCompat.from(fragment.requireActivity()).areNotificationsEnabled();

        if (areNotificationsEnabled && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = NotificationManagerCompat.from(fragment.requireActivity()).getNotificationChannel(SyncRateJobService.CHANNEL_ID);

            if (channel!=null && channel.getImportance()==IMPORTANCE_NONE)
                areNotificationsEnabled = false;
        }
        getNotificationStateEvent.onNext(areNotificationsEnabled);
    }

    @Override
    public void showAppSettingsNotificationsDialog() {
        showAppSettingsDialog(RC_APP_SETTINGS_NOTIFICATION_DIALOG, R.string.app_settings_notifications_message);
    }

    private void showAppDetailSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse("package:" + fragment.requireActivity().getPackageName()));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        fragment.startActivity(intent);
    }

    @Override
    public void detach() {
        compositeDisposable.dispose();
        this.fragment = null;
    }

    @Override
    public Observable<Fiat> onCurrencySelectedEvent() {
        return onCurrencySelectedEvent;
    }

    @Override
    public Observable<String> onJobStartedEvent() {
        return onJobStartedEvent;
    }

    @Override
    public Observable<Object> onJobStoppedEvent() {
        return onJobStoppedEvent;
    }

    @Override
    public Observable<String> onConfirmationRateNotifyOnEvent() {
        return onConfirmationRateNotifyOnEvent;
    }

    @Override
    public Observable<Object> onConfirmationRateNotifyOffEvent() {
        return onConfirmationRateNotifyOffEvent;
    }

    @Override
    public Observable<Boolean> getNotificationStateEvent() {
        return getNotificationStateEvent;
    }

    @Override
    public Observable<Object> onRequestPermissionTrueEvent() {
        return onRequestPermissionTrueEvent;
    }

    @Override
    public Observable<Integer> onRequestPermissionFalseEvent() {
        return onRequestPermissionFalseEvent;
    }

    @Override
    public Observable<Integer> onRequestNotificationFalseEvent() {
        return onRequestNotificationFalseEvent;
    }

    @Override
    public Observable<Integer> onCreatePdfFalseEvent() {
        return onCreatePdfFalseEvent;
    }

    @Override
    public void syncRateJobStarted(String symbol) {
        onJobStartedEvent.onNext(symbol);
    }

    @Override
    public void syncRateJobStopped() {
        onJobStoppedEvent.onNext(new Object());
    }
}
