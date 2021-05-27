package com.vritti.freshmart.customer;

import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetDialog;
import android.os.StrictMode;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;

import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.VideoView;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;
import com.squareup.picasso.Picasso;
import com.vritti.freshmart.CompanyUrlActivity;
import com.vritti.freshmart.GetNewaddress;
import com.vritti.freshmart.GpsLocationActivity;

import com.vritti.freshmart.MultiMerchant.BusinessSegmentListActivity;
import com.vritti.freshmart.MultiMerchant.ItemListActivity_Multimerchant;
import com.vritti.freshmart.MultiMerchant.MerchantSelectionActivity;
import com.vritti.freshmart.MultiMerchant.Multimerchant_ProductListActivity;
import com.vritti.freshmart.ProgressHUD;
import com.vritti.freshmart.R;
import com.vritti.freshmart.SplashActivity;
import com.vritti.freshmart.UserProfileUpdateActivity;
import com.vritti.freshmart.adapters.CategoryAdapter;
import com.vritti.freshmart.adapters.CustomListAdapter;
import com.vritti.freshmart.adapters.OpenOrderListAdapter;
import com.vritti.freshmart.adapters.RecentOrderedListAdapter;
import com.vritti.freshmart.adapters.ShipToRecyclerAdapter;
import com.vritti.freshmart.adapters.Sliding_Image_Adapter;
import com.vritti.freshmart.adapters.URL_ListAdapter;
import com.vritti.freshmart.adapters.WishListAdapter;
import com.vritti.freshmart.beans.*;
import com.vritti.freshmart.classes.Connectiondetector;
import com.vritti.freshmart.classes.DatabaseHandler;
import com.vritti.freshmart.classes.URL_Company_Domain;
import com.vritti.freshmart.data.*;
import com.vritti.freshmart.utils.*;
import com.vritti.freshmart.database.*;
import com.vritti.freshmart.interfaces.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static com.google.firebase.crash.FirebaseCrash.log;
import static com.vritti.freshmart.R.layout.*;

//-------------------------------------Customer Main Activity------------------------------------------//
public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {
    TextView txtusername, txtmobileno, txtownername,txt_cartcount,txtappversion;
    ImageView imgcart;
    LinearLayout lay_category,coach_mark_master_view;
    Button btnchangemode;
    ImageButton btnsrch_server;
    ToggleButton account_view_icon_button;
    AutoCompleteTextView edtsearch_product;
    NavigationView navigationView;
   // private ListView listview;
    private GridView listview;
    LinearLayout lay_update_app;
    LinearLayout txtnoordnote;
    TextView txtlaunchsoon,selprodcat;
    Button btndeladdr,btngotit;

    private static int newlistCount = 0;
    private static Context parent;
    public static ArrayList<AllCatSubcatItems> arrayList;
    public static ArrayList<AllCatSubcatItems> arrayList_subcat;
    public static ArrayList<OrderHistoryBean> historyBeanList;
    static ArrayList<OrderHistoryBean> newList = null;
    public static ArrayList<OrderHistoryBean> historyBeanList_new;
    public static ArrayList<MyCartBean> arrayList_bean;
    private int backpressCount = 0;
    private AllCatSubcatItems bean;
    private static String json;
    private long back_pressed = 0;
    private static DatabaseHelper databaseHelper;
    static ProgressHUD progress;
    public String restoredText, restoredusername, restoredownername, usertype, domainname;
    SharedPreferences sharedpreferences;
    private RelativeLayout coordinatorLayout;
    public static String Mobilenumber;
    String message, ataaleliurl;
    ArrayList<URL_Company_Domain> URL_list;
    //ArrayList<String> URL_list;
    private URL_Company_Domain bean_url;
    private DatabaseHelper_URLStore db_URLStore;
    SQLiteDatabase sql_db;
    URL_ListAdapter urlAdapter;
    ListView navheader_accountslist;
    String CustVendorMasterId, CustomerID;
    String res = "";

    //viewpager code
    ViewPager viewPager;
    PagerAdapter pagerAdapter;
    int[] imgflag;
    final Handler handler = new Handler();
    public Timer swipeTimer;

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;

    public ImageView iv;

    public static final int[] imgthumbIds_ = {R.drawable.banner_orange,R.drawable.banner_orange};

    public static final String[] imgthumbIds = {"http://prasad.ekatm.com/Images/snacks.png",
            "http://prasad.ekatm.com/Images/rice.png","http://prasad.ekatm.com/Images/tea_tc.png"};

    ArrayList<Banner> bannerList;
    ArrayList<String> bannerDispList;

    Sliding_Image_Adapter mycustmadapter;
    int i = 0;
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();

    private ImageSwitcher imageSwitcher;
    private int animationCounter = 1;
    private Handler imageSwitcherHandler;
    LinearLayout recentorderlistlayout, startshopping;
    TextView txt_showmore, noorder;
    static GridView listview_recent_ordered_list;
    ImageView imageview_goto;
    String cat_name, cat_id,PurDigit,CatImgPath = "";
    int SubCatCount = 0;
    WishListAdapter wAdapter;
    RecentOrderedListAdapter RAdapter;
    MyCartBean myCartBean;
    static OpenOrderListAdapter myOrderHistoryAdapter;
    static TextView pending_ordrcnt;
    String CopmanyURL;

    int hot_number = 0;
    TextView ct_count;
    String dialogopen = "no";
    static int Year, month, day;
    String TODAYDATE, FLAG_PSTORE;

    ////////////////////////////////////////////////////////////////////////////////////////////////

    Connectiondetector cd;
    private String jimageview_mainon_1, jsonRate;
    private ArrayList<Merchants_against_items> merchants_against_itemsArrayList;
    private Merchants_against_items merchants_against_items;
    public static String today;
    DatabaseHandler dbHandler;
    private static String DateToStr;
    private String DateToString;
    private String DateToString_ack;
    private static int index = 0;
    private Uri imageUri;
    Dialog dialog;
    TextView txtupdateversion,btnupdateversion;
    Timer timer;
    final long DELAY_MS = 5000;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 5000;

    String currentLanguage = "en", currentLang;
    Locale myLocale;
    RecyclerView rvlist;
    ListView recyclerView;
    LinearLayout casts_container;
    TextView txtdelto;
    ArrayList<Customer> addrlist = new ArrayList<>();
    LinearLayout clickableColumn = null;
    TextView titleView = null;
    ShipToRecyclerAdapter shrv;
    TextView txt_lastmerch,plcwith;
    LinearLayout laybtn_bymerch;

    boolean isDelAddrSelected;
    BottomSheetDialog btmsheetdialog_addr;
    String subCatId_directserch = "";
    String pathVideo = "";
    VideoView videoView;

    String lastOrd_MerchName = "", lastOrd_MerchId = "";
    public int SegmentSelection = 2;
    boolean shopByMerchant = false;
    boolean DirectSegmentSearch = false;

    public static final int REQ_CODE_VERSION_UPDATE = 530;
    public AppUpdateManager appUpdateManager;
    Task<AppUpdateInfo> appUpdateInfoTask;

    String appURI = "";

    private DownloadManager downloadManager;
    private long downloadReference;

    // Declare the UpdateManager
    //UpdateManager mUpdateManager;
    CategoryAdapter cadt;

    ArrayList<AllCatSubcatItems> serchProd;
    ArrayList<AllCatSubcatItems> serchProd1;
    ArrayList<AllCatSubcatItems> tempList;
    ArrayList<String> serchProdstr;
    ArrayList<String> serchProdstr1;
    ArrayList<String> tempList1;
    ArrayAdapter<String> search_adapter;

    private int verticalOffset;
    private int discardmsg_dispcnt = 0;

    int playstoreflag = 0;
    String selectedCatid="",selectedCatName="";
    //Search_Prod_BrandAdapter adt;
    CustomListAdapter adt;
    String diayn="", dicurDate="";
    public JSONArray jrresult;
    public JSONArray jrresult_supp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        initialize();

        AppBarLayout mAppBarLayout = (AppBarLayout) findViewById(R.id.appbarlayout);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            boolean isShow = false;
            int scrollRange = -1;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {

                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    isShow = true;
                    // showOption(R.id.action_info);
                } else if (isShow) {
                    isShow = false;
                    // hideOption(R.id.action_info);
                }
            }
        });

        callforplayStore();

       /* if (diayn.equalsIgnoreCase("YesDialog")) {

            Date date = new Date();
            final Calendar c = Calendar.getInstance();

            Year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);

            String New_TodayDate = day + "-" + (month + 1) + "-" + Year;

            if (New_TodayDate.equalsIgnoreCase(dicurDate)) {
                FLAG_PSTORE = "DONT_SHOW_DIALOG";
            } else {
                FLAG_PSTORE = "SHOW_DIALOG";
            }

            if (FLAG_PSTORE.equalsIgnoreCase("SHOW_DIALOG")) {
                //if date not matched then call
                callforplayStore();
            } else {
                //for forcefully update
                callforplayStore();
            }
        }else {
             SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("Dialog", "YesDialog");
            editor.apply();
        }*/

        if(getShiptolist() == 0){
            if(isnet()){
                new StartSession(parent, new CallbackInterface() {
                    @Override
                    public void callMethod() {

                        new DownloadShipToDataJSON().execute();
                    }
                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        }else {
            if(isDelAddrSelected == false){
                openDialogueDelBox();
            }else {
                txtdelto.setText(""+getResources().getString(R.string.deliverto)+" "+ AnyMartData.ADDRESS);
            }
        }

        if(AnyMartData.AppCode.equalsIgnoreCase("SM")){
             //getDataFromDatabase_pendingorders();
        }

        getDataFromServer();

        int count = 0;
        count = databaseHelper.getCartItems();

        //show dialog msg

        if(count > 0){

            if(discardmsg_dispcnt == 0){
                dialog = new Dialog(parent);
                dialog.setContentView(R.layout.dialog_message);
                TextView txtMsg = (TextView) dialog.findViewById(R.id.textMsg);
                Button btnyes = (Button) dialog.findViewById(R.id.btn_yes);
                Button btnno = (Button) dialog.findViewById(R.id.btn_no);
                EditText edtreason =  dialog.findViewById(R.id.edtreason);
                edtreason.setVisibility(View.GONE);
                txtMsg.setText(getResources().getString(R.string.dilg_discardcart));
                dialog.show();

                btnyes.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Utilities.clearTable(parent, AnyMartDatabaseConstants.TABLE_CART_ITEM);
                        Toast.makeText(parent,""+getResources().getString(R.string.cart_discarded),Toast.LENGTH_LONG).show();

                        int count = 0;
                        count = databaseHelper.getCartItems();
                        txt_cartcount.setText("0");

                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putInt("DiscardCart_PopUp", 1);
                        editor.commit();

                        dialog.dismiss();
                    }
                });

                btnno.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putInt("DiscardCart_PopUp", 1);
                        editor.commit();

                        dialog.dismiss();
                    }
                });
            }else {

            }
        }

        setListeners();
    }

    /////////////////////////////////////////////////// addmerchant database code //////////////////////////////////////////////////////

    public static class GetPendingOrderHistoryList extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String responseString = "";
        String resp_orderHistory = "";
        String res = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* progressDialog = new ProgressDialog(parent);
            progressDialog.setMessage("Loading Pending Orders...");
            progressDialog.show();*/
           try{
               progress = ProgressHUD.show(parent, ""+parent.getResources().getString(R.string.loading_pending_ords),
                       false, true, null);
               progress.setCanceledOnTouchOutside(true);
               progress.setCancelable(true);
           }catch (Exception e){
               e.printStackTrace();
           }
        }

        @Override
        protected String doInBackground(String... params) {
            /*String url_orderHistory = AnyMartData.MAIN_URL + AnyMartData.METHOD_PENDING_ORDER_HISTORY +
                    "?mobileno=" + AnyMartData.MOBILE +
                    "&handler=" + AnyMartData.HANDLE +
                    "&sessionid=" + AnyMartData.SESSION_ID +
                    "&index=" + index;*/

            String url_orderHistory = AnyMartData.MAIN_URL + AnyMartData.METHOD_ORDER_HISTORY +
                    "?mobileno="+ AnyMartData.MOBILE + "&statuscode=10&handler="+ AnyMartData.HANDLE +
                    "&sessionid="+ AnyMartData.SESSION_ID ;

            URLConnection urlConnection = null;
            BufferedReader bufferedReader = null;

            try {
                res = Utility.OpenconnectionOrferbilling(url_orderHistory, parent);
                int a = res.getBytes().length;
                res = res.replaceAll("\\\\", "");
                // res = res.replaceAll("\"", "");
                // res = res.replaceAll(" ", "");
                responseString = res.toString().replaceAll("^\"|\"$", "");
                Log.e("Response", responseString);

                resp_orderHistory = responseString.replaceAll("\\\\", "");
                System.out.println("rsep = " + resp_orderHistory);

            } catch (NullPointerException e) {
                resp_orderHistory = "empty";
                e.printStackTrace();
            } catch (Exception e) {
                resp_orderHistory = "error";
                e.printStackTrace();
            }
            return resp_orderHistory;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try{
                progress.dismiss();
            }catch (Exception e){
                e.printStackTrace();
            }

            try{
                if (resp_orderHistory.equalsIgnoreCase("Session Expired")) {
                    if (NetworkUtils.isNetworkAvailable(parent)) {
                        new StartSession(parent, new CallbackInterface() {
                            @Override
                            public void callMethod() {

                                new GetPendingOrderHistoryList().execute();
                            }

                            @Override
                            public void callfailMethod(String s) {

                            }
                        });
                    }

                } else if (resp_orderHistory.equalsIgnoreCase("[]")) {
                    // noorder.setVisibility(View.VISIBLE);
                    Toast.makeText(parent, ""+parent.getResources().getString(R.string.no_ord_place_on), Toast.LENGTH_LONG).show();
                }  else if (resp_orderHistory.equalsIgnoreCase("empty")) {
                    // noorder.setVisibility(View.VISIBLE);
                    Toast.makeText(parent, ""+parent.getResources().getString(R.string.no_ord_place_on), Toast.LENGTH_LONG).show();
                } else if (resp_orderHistory.equalsIgnoreCase("error")) {
                    Toast.makeText(parent, ""+parent.getResources().getString(R.string.servererror), Toast.LENGTH_LONG).show();
                } else {
                    json = resp_orderHistory;
                    parseJson_pendingorder(json);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private boolean isnet() {
        // TODO Auto-generated method stub
        Context context = this.getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    protected static void parseJson_pendingorder(String json) {
        Utilities.clearTable_OrdHistory(parent, AnyMartDatabaseConstants.TABLE_MY_ORDER_HISTORY,"10");
        //arrayList.clear();

        SQLiteDatabase sql  = databaseHelper.getWritableDatabase();

        String OrdRCVDt_ModifiedDt = "";

        try {

            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {

                String SalesHeaderId = jsonArray.getJSONObject(i).getString("SalesHeaderId");
                String ShipStatus = jsonArray.getJSONObject(i).getString("ShipStatus");
                String OrdRcvdDate = jsonArray.getJSONObject(i).getString("ModifiedDt");
                String DODisptch = jsonArray.getJSONObject(i).getString("DODisptch");

                String PrefDelFrmTime = "", PrefDelToTime = "";
                try{
                    PrefDelFrmTime = jsonArray.getJSONObject(i).getString("PrefDelFrmTime");
                    PrefDelToTime = jsonArray.getJSONObject(i).getString("PrefDelToTime");
                }catch (Exception e){
                    e.printStackTrace();
                    PrefDelFrmTime = "";
                    PrefDelToTime = "";
                }

                String DispNetAmnt = jsonArray.getJSONObject(i).getString("DispNetAmnt");
                String DispatchNo = jsonArray.getJSONObject(i).getString("DispatchNo");
                String sono = jsonArray.getJSONObject(i).getString("sono");
                String statusname = jsonArray.getJSONObject(i).getString("statusname");
                String SODate = jsonArray.getJSONObject(i).getString("SODate");
                String status = jsonArray.getJSONObject(i).getString("status");
                String a = jsonArray.getJSONObject(i).getString("status");
                String CustomerMasterId = jsonArray.getJSONObject(i).getString("CustomerMasterId");
                String ShipToMasterId = jsonArray.getJSONObject(i).getString("ShipToMasterId");
                String Destination = jsonArray.getJSONObject(i).getString("Destination").trim();
                String ConsigneeName = jsonArray.getJSONObject(i).getString("ConsigneeName");
                String Address = jsonArray.getJSONObject(i).getString("Address");
                String City = jsonArray.getJSONObject(i).getString("City");
                String State = jsonArray.getJSONObject(i).getString("State");
                String Country = jsonArray.getJSONObject(i).getString("Country");
                String Mobile = jsonArray.getJSONObject(i).getString("Mobile");
                String DObkd = jsonArray.getJSONObject(i).getString("DObkd");
                String placeOrderDate = jsonArray.getJSONObject(i).getString("DoAck");
                String DORcvd = jsonArray.getJSONObject(i).getString("DORcvd");
                String DOrej = jsonArray.getJSONObject(i).getString("DOrej");
                String AppvDt = jsonArray.getJSONObject(i).getString("AppvDt");
                String SODetailId = jsonArray.getJSONObject(i).getString("SODetailId");
                String SOHeaderId = jsonArray.getJSONObject(i).getString("SOHeaderId");
                // String Dispatchdt = jsonArray.getJSONObject(i).getString("Dispatchdt");
                String NetAmt = jsonArray.getJSONObject(i).getString("NetAmt");

                String OrgQty = jsonArray.getJSONObject(i).getString("OrgQty");
                String DeliveryTerms = jsonArray.getJSONObject(i).getString("DeliveryTerms");
                String minordqty = jsonArray.getJSONObject(i).getString("minordqty");
                String maxordqty = jsonArray.getJSONObject(i).getString("maxordqty");
                String distance = jsonArray.getJSONObject(i).getString("distance");
                String UOMCode = jsonArray.getJSONObject(i).getString("UOMCode");
                String outofstock = jsonArray.getJSONObject(i).getString("outofstock");
                String mrp = jsonArray.getJSONObject(i).getString("mrp");
                String sellingrate = jsonArray.getJSONObject(i).getString("sellingrate");
                String range = jsonArray.getJSONObject(i).getString("range");
                String UOMDigit = jsonArray.getJSONObject(i).getString("UOMDigit");

                SimpleDateFormat Format = new SimpleDateFormat("MMM dd yyyy");//Feb 23 2016 12:16PM
                //SimpleDateFormat Format = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
                //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
                SimpleDateFormat toFormat = new SimpleDateFormat("MM/dd/yyyy");
                Date d1 = format.parse(placeOrderDate);

                if(OrdRcvdDate.equalsIgnoreCase("null")){

                }else {
                    if(!OrdRcvdDate.equalsIgnoreCase("")){
                        Date d2 = format.parse(OrdRcvdDate);
                        OrdRCVDt_ModifiedDt = Format.format(d2);
                    }
                }
                //DateToStr = toFormat.format(date);
                DateToStr = Format.format(d1);

                // DateToStr = format.format(d1);
                System.out.println(DateToStr);

                OrderHistoryBean historybean = new OrderHistoryBean();
                historybean.setSOHeaderId(SOHeaderId);
                historybean.setConsigneeName(ConsigneeName);
                historybean.setSODate(SODate);
                historybean.setNetAmt(Float.parseFloat(NetAmt));
                historybean.setDoAck(placeOrderDate);
                historybean.setStatus(status);
                historybean.setSONo(sono);
                historybean.setAddress(Address);

                historyBeanList.add(historybean);

                databaseHelper.addOrderHistory(jsonArray.getJSONObject(i).getString(
                        "Address"), jsonArray.getJSONObject(i).getString(
                        "City"), jsonArray.getJSONObject(i).getString(
                        "ConsigneeName"), jsonArray.getJSONObject(i).getString(
                        "CustomerMasterId"), jsonArray.getJSONObject(i).getString(
                        "ItemMasterId"), jsonArray.getJSONObject(i).getString(
                        "Mobile"), jsonArray.getJSONObject(i).getString(
                        "Qty"), jsonArray.getJSONObject(i).getString(
                        "Rate"), jsonArray.getJSONObject(i).getString(
                        "SODate"), jsonArray.getJSONObject(i).getString(
                        "SOHeaderId"), jsonArray.getJSONObject(i).getString(
                        "DODisptch"), jsonArray.getJSONObject(i).getString(
                        "DORcvd"), jsonArray.getJSONObject(i).getString(
                        "status"),jsonArray.getJSONObject(i).getString(
                        "statusname"), jsonArray.getJSONObject(i).getString(
                        "DoAck"), jsonArray.getJSONObject(i).getString(
                        "NetAmt"), jsonArray.getJSONObject(i).getString(
                        "ItemDesc"),jsonArray.getJSONObject(i).getString(
                        "LineAmt"),jsonArray.getJSONObject(i).getString(
                        "merchantid"),jsonArray.getJSONObject(i).getString(
                        "merchantname"),jsonArray.getJSONObject(i).getString(
                        "SODetailId"),jsonArray.getJSONObject(i).getString(
                        "sono"),jsonArray.getJSONObject(i).getString(
                        "SOScheduleId"),jsonArray.getJSONObject(i).getString(
                        "ShipmentQty"),jsonArray.getJSONObject(i).getString(
                        "ClientRecQty"),jsonArray.getJSONObject(i).getString(
                        "AppvDt"),UOMDigit,"",
                        jsonArray.getJSONObject(i).getString("DispatchNo"),
                        jsonArray.getJSONObject(i).getString("SalesHeaderId"),"",
                        jsonArray.getJSONObject(i).getString("DispNetAmnt"),
                        jsonArray.getJSONObject(i).getString("ShipStatus"),
                        jsonArray.getJSONObject(i).getString("ModifiedDt"),
                        DateToStr, PrefDelFrmTime,PrefDelToTime,"","",
                        OrgQty,DeliveryTerms,minordqty, maxordqty,distance,UOMCode,outofstock,mrp,sellingrate,range,
                        "","","","","0","",
                        "","","","","",
                        "","","","","",
                        "", "", "","","","");
            }

            /*myOrderHistoryAdapter = new OpenOrderListAdapter(parent, historyBeanList);
            listview_recent_ordered_list.setAdapter(myOrderHistoryAdapter);*/

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        getpndingOrdData();

    }

    private void getDataFromDatabase_pendingorders() {
        historyBeanList.clear();

        SQLiteDatabase sql = databaseHelper.getWritableDatabase();

        Cursor c = sql.rawQuery(
                "SELECT distinct SOHeaderId,ConsigneeName FROM " + AnyMartDatabaseConstants.TABLE_MY_ORDER_HISTORY
                        + " WHERE Mobile ='"
                        + AnyMartData.MOBILE + "' ORDER BY DoAck desc ",
                null);
        int ordercnt = 0;
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {

                String orderid = c.getString(c.getColumnIndex("SOHeaderId"));
                ordercnt = ordercnt + 1;
                OrderHistoryBean historybean = new OrderHistoryBean();

                historybean.setSOHeaderId/*(Integer.parseInt*/(orderid); //String.valueOf(ordercnt)
                historybean.setConsigneeName(c.getString(c
                        .getColumnIndex("ConsigneeName")));
                SQLiteDatabase sql1 = databaseHelper.getWritableDatabase();
                Cursor c1 = sql1.rawQuery(
                        "SELECT distinct SODate , NetAmt, DODisptch, DORcvd, status,statusname, DoAck FROM "
                                + AnyMartDatabaseConstants.TABLE_MY_ORDER_HISTORY + " WHERE SOHeaderId ='"
                                + orderid + "' ORDER BY date(DoAck) desc ",
                        null);
                float amt = 0;
                if (c1.getCount() > 0) {
                    c1.moveToFirst();
                    do {
                        //   float amtofitem = c1.getFloat(c1.getColumnIndex("Rate"));
                        String o_date = c1.getString(c1.getColumnIndex("SODate"));
                        //   amt = amt + amtofitem;

                        historybean.setSODate(o_date);
                      //  historybean.setRate(Float.parseFloat(c1.getString(c1.getColumnIndex("NetAmt"))));
                        historybean.setDoAck(c1.getString(c1.getColumnIndex("DoAck")));
                        historybean.setDODisptch(c1.getString(c1.getColumnIndex("DODisptch")));
                        //historybean.setDORcvd(c1.getString(c1.getColumnIndex("DORcvd")));
                        historybean.setDORcvd(c1.getString(c1.getColumnIndex("OrdRcvdDate")));
                        historybean.setStatus(c1.getString(c1.getColumnIndex("status")));
                        historybean.setStatusname(c1.getString(c1.getColumnIndex("statusname")));

                    } while (c1.moveToNext());
                }
                historyBeanList.add(historybean);

            } while (c.moveToNext());
        } else {

        }

        myOrderHistoryAdapter = new OpenOrderListAdapter(parent, historyBeanList);
        listview_recent_ordered_list.setAdapter(myOrderHistoryAdapter);
        pending_ordrcnt.setText(index);

        /*myOrderHistoryAdapter = new MyOrderHistoryAdapter(MainActivity.this, historyBeanList);
        listview_my_orders_history.setAdapter(myOrderHistoryAdapter);*/
    }

    private void getDataFromServer_addMerchant() {
        if (NetworkUtils.isNetworkAvailable(parent)) {
            /*if ((AnyMartData.SESSION_ID != null)
                    && (AnyMartData.HANDLE != null)) {
                new GetMerchants().execute();
            } else {*/
                new StartSession(MainActivity.this, new CallbackInterface() {

                    @Override
                    public void callMethod() {
                        new GetMerchants().execute();
                    }

                    @Override
                    public void callfailMethod(String s) {

                    }
                });
           // }
        } else {
            Toast.makeText(parent, ""+getResources().getString(R.string.servererror), Toast.LENGTH_LONG).show();
        }
    }

    class GetMerchants extends AsyncTask<String, Void, String> {
        String responseString = "";
        String resp_addMerchant = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* progressDialog = new ProgressDialog(parent);
            progressDialog.setMessage("Get Merchants...");
            progressDialog.show();*/
            /*progress = ProgressHUD.show(MainActivity.this,
                    "Get Merchants...", false, true, null);*/
        }

        @Override
        protected String doInBackground(String... params) {
            String url_AddMerchant = AnyMartData.MAIN_URL + AnyMartData.METHOD_GET_MERCHANT +
                    "?sessionid=" + AnyMartData.SESSION_ID +
                    "&handler=" + AnyMartData.HANDLE +
                    //"&type=" + usertype + "&city=" + AnyMartData.CITY;
                    "&type=" + "V" + "&city=" + AnyMartData.CITY;
            URLConnection urlConnection = null;
            BufferedReader bufferedReader = null;

            try {
                res = Utility.OpenconnectionOrferbilling(url_AddMerchant, parent);
                int a = res.getBytes().length;
                res = res.replaceAll("\\\\", "");
                // res = res.replaceAll("\"", "");
                // res = res.replaceAll(" ", "");
                responseString = res.toString().replaceAll("^\"|\"$", "");
                Log.e("Response", responseString);

                resp_addMerchant = responseString.replaceAll("\\\\", "");

                System.out.println("resp =" + resp_addMerchant);
            } catch (Exception e) {
                resp_addMerchant = "error";
                e.printStackTrace();
            }
            return resp_addMerchant;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try{
                if (resp_addMerchant.equalsIgnoreCase("Session Expired")) {
                    progress.dismiss();
                    if (cd.isConnectingToInternet()) {
                        new StartSession(MainActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new GetMerchants().execute();
                            }

                            @Override
                            public void callfailMethod(String s) {

                            }
                        });
                    }
                } else if (resp_addMerchant.equalsIgnoreCase("error")) {
                    progress.dismiss();
                    Toast.makeText(parent, ""+getResources().getString(R.string.servererror), Toast.LENGTH_LONG)
                            .show();
                } else {
                    //    progress.dismiss();
                    jsonRate = resp_addMerchant;
                    parseJson_addmerchant(jsonRate);
                    //parseJson(JSON);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    protected void parseJson_addmerchant(String json) {
        merchants_against_itemsArrayList.clear();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {

                String fromdate = get_date(jsonArray.getJSONObject(i).getString("validfrom"));
                String todate = get_date(jsonArray.getJSONObject(i).getString("validto"));
                String discount = "";
                String MerchantName = jsonArray.getJSONObject(i).getString("MrchtName");
                String MerchantAddress = jsonArray.getJSONObject(i).getString("MerchantAddress");
                String MerchantEmail = jsonArray.getJSONObject(i).getString("MerchantEmail");
                String MerchantMobile = jsonArray.getJSONObject(i).getString("MerchantMobile");

                if (!(jsonArray.getJSONObject(i)
                        .getString("discratemrp").equalsIgnoreCase("NA"))
                        || (!(jsonArray.getJSONObject(i)
                        .getString("discratepercent").equalsIgnoreCase("NA")))) {

                    if (!(jsonArray.getJSONObject(i)
                            .getString("discratemrp").equalsIgnoreCase("NA"))) {

                        discount = jsonArray.getJSONObject(i)
                                .getString("discratemrp") + " ₹";

                    } else if (!(jsonArray.getJSONObject(i)
                            .getString("discratepercent").equalsIgnoreCase("NA"))) {

                        discount = jsonArray.getJSONObject(i)
                                .getString("discratepercent") + " %";
                    }
                } else {
                    discount = "0 ₹";
                }

                if (jsonArray.getJSONObject(i)
                        .getString("validfrom").equalsIgnoreCase("NA") &&
                        jsonArray.getJSONObject(i)
                                .getString("validto").equalsIgnoreCase("NA")
                        && (!(String.valueOf(jsonArray.getJSONObject(i)
                        .getString("MRP"))).equalsIgnoreCase("NA"))) {

                    merchants_against_items = new Merchants_against_items();
                    merchants_against_items.setMerchant_name_two(MerchantName);
                    merchants_against_items.setMerchnat_address(MerchantAddress);
                    merchants_against_items.setMerchnat_email(MerchantEmail);
                    merchants_against_items.setMerchnat_mobile(MerchantMobile);

                    merchants_against_items.setMerchant_name(jsonArray.getJSONObject(i)
                            .getString("custvendorname"));
                    merchants_against_items.setQnty(jsonArray.getJSONObject(i)
                            .getInt("minqty"));
                    merchants_against_items.setMinqnty(jsonArray.getJSONObject(i)
                            .getInt("minqty"));
                    merchants_against_items.setOffers(discount);
                    merchants_against_items.setPrice(Float.parseFloat(jsonArray.getJSONObject(i)
                            .getString("MRP")));

                    merchants_against_items.setMerchant_id(jsonArray.getJSONObject(i)
                            .getString("fkvendorid"));
                    if (jsonArray.getJSONObject(i)
                            .getString("freeitemid").equalsIgnoreCase("NA")) {
                        merchants_against_items.setFreeitemid("");
                        merchants_against_items.setFreeitemqty(0);
                        merchants_against_items.setFreeitemname("");
                    } else {
                        merchants_against_items.setFreeitemid(jsonArray.getJSONObject(i)
                                .getString("freeitemid"));
                        merchants_against_items.setFreeitemqty(jsonArray.getJSONObject(i)
                                .getInt("freeitemqty"));
                        String freeitemname = getFreeItemname(jsonArray.getJSONObject(i)
                                .getString("freeitemid"));
                        merchants_against_items.setFreeitemname(freeitemname);

                    }
                    merchants_against_items.setValidfrom(fromdate);

                    merchants_against_items.setValidto(todate);
                    merchants_against_items.setProduct_name(jsonArray.getJSONObject(i)
                            .getString("vendoritemname"));


                    databaseHelper.addMerchants(merchants_against_items.getMerchant_id(),
                            merchants_against_items.getMerchant_name(),
                            merchants_against_items.getQnty(), merchants_against_items.getMinqnty(),
                            merchants_against_items.getOffers(),
                            merchants_against_items.getPrice(),
                            merchants_against_items.getProduct_name(), merchants_against_items.getFreeitemid(),
                            merchants_against_items.getFreeitemqty(), merchants_against_items.getFreeitemname(),
                            merchants_against_items.getValidfrom(), merchants_against_items.getValidto(),
                            merchants_against_items.getMerchant_name_two(), merchants_against_items.getMerchnat_address(),
                            merchants_against_items.getMerchnat_email(), merchants_against_items.getMerchnat_mobile());

                    merchants_against_itemsArrayList.add(merchants_against_items);

                } else if (compare_date(fromdate, todate) == true &&
                        (!(String.valueOf(Float.parseFloat(jsonArray.getJSONObject(i)
                                .getString("MRP")))).equalsIgnoreCase("NA"))) {


                    merchants_against_items = new Merchants_against_items();

                    merchants_against_items.setMerchant_name_two(MerchantName);
                    merchants_against_items.setMerchnat_address(MerchantAddress);
                    merchants_against_items.setMerchnat_email(MerchantEmail);
                    merchants_against_items.setMerchnat_mobile(MerchantMobile);

                    merchants_against_items.setMerchant_name(jsonArray.getJSONObject(i)
                            .getString("custvendorname"));


                    merchants_against_items.setQnty(jsonArray.getJSONObject(i)
                            .getInt("minqty"));
                    merchants_against_items.setMinqnty(jsonArray.getJSONObject(i)
                            .getInt("minqty"));


                    merchants_against_items.setOffers(discount);
                    merchants_against_items.setPrice(Float.parseFloat(jsonArray.getJSONObject(i)
                            .getString("MRP")));

                    merchants_against_items.setMerchant_id(jsonArray.getJSONObject(i)
                            .getString("fkvendorid"));
                    if (jsonArray.getJSONObject(i)
                            .getString("freeitemid").equalsIgnoreCase("NA")) {
                        merchants_against_items.setFreeitemid("");
                        merchants_against_items.setFreeitemqty(0);
                        merchants_against_items.setFreeitemname("");
                    } else {
                        merchants_against_items.setFreeitemid(jsonArray.getJSONObject(i)
                                .getString("freeitemid"));
                        merchants_against_items.setFreeitemqty(jsonArray.getJSONObject(i)
                                .getInt("freeitemqty"));
                        String freeitemname = getFreeItemname(jsonArray.getJSONObject(i)
                                .getString("freeitemid"));
                        merchants_against_items.setFreeitemname(freeitemname);

                    }
                    merchants_against_items.setValidfrom(fromdate);

                    merchants_against_items.setValidto(todate);
                    merchants_against_items.setProduct_name(jsonArray.getJSONObject(i)
                            .getString("vendoritemname"));

                    databaseHelper.addMerchants(merchants_against_items.getMerchant_id(),
                            merchants_against_items.getMerchant_name(),
                            merchants_against_items.getQnty(),
                            merchants_against_items.getMinqnty(),
                            merchants_against_items.getOffers(),
                            merchants_against_items.getPrice(),
                            merchants_against_items.getProduct_name(),
                            merchants_against_items.getFreeitemid(),
                            merchants_against_items.getFreeitemqty(),
                            merchants_against_items.getFreeitemname(),
                            merchants_against_items.getValidfrom(),
                            merchants_against_items.getValidto(),
                            merchants_against_items.getMerchant_name_two(),
                            merchants_against_items.getMerchnat_address(),
                            merchants_against_items.getMerchnat_email(),
                            merchants_against_items.getMerchnat_mobile());

                    merchants_against_itemsArrayList.add(merchants_against_items);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //getDataFromDataBase_addmerchant();
    }

    public String get_date(String d) {

        String finalDate;
        if (!(d.equals("") || d == null)) {

            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
            //SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

            Date myDate = null;
            try {
                myDate = dateFormat.parse(d);
                //myDate = readFormat.parse(d);
                System.out.println("..........value of my date after conv"+ myDate);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat timeFormat = new SimpleDateFormat("MMM dd yyyy");
            finalDate = timeFormat.format(myDate);

           /* String formattedDate = "";
            if( myDate != null ) {
                finalDate = writeFormat.format( myDate );
            }

            System.out.println(finalDate);*/

        } else {
            finalDate = "";
        }

        return finalDate;
    }

    public static boolean compare_date(String fromdate, String todate) {
        boolean b = false;
        SimpleDateFormat dfDate = new SimpleDateFormat("MMM dd yyyy");

        today = dfDate.format(new Date());
        try {
            if ((dfDate.parse(today).after(dfDate.parse(fromdate)) ||
                    dfDate.parse(today).equals(dfDate.parse(fromdate))) &&
                    (dfDate.parse(today).before(dfDate.parse(todate)) ||
                            dfDate.parse(today).equals(dfDate.parse(todate)))) {
                b = true;
            } else {
                b = false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return b;
    }

    private String getFreeItemname(String id) {
        DatabaseHelper db1 = new DatabaseHelper(parent, AnyMartDatabaseConstants.DATABASE__NAME_URL);
        SQLiteDatabase db = db1.getWritableDatabase();
        String que = "Select  ItemName  from "
                + AnyMartDatabaseConstants.TABLE_ALL_CAT_SUBCAT_ITEMS +
                " where ItemMasterId='" + id + "'";
        String itemname;
        Cursor c = db.rawQuery(que, null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            itemname = c.getString(c.getColumnIndex("ItemName"));
        } else {
            itemname = "";
        }

        return itemname;
    }

  /*  private void getDataFromDataBase_addmerchant() {
        // TODO Auto-generated method stub
        arrayList.clear();

        DatabaseHelper db1 = new DatabaseHelper(parent);
        SQLiteDatabase db = db1.getWritableDatabase();

        Cursor c = db.rawQuery("Select distinct MerchantId,MerchantName from "
                + AnyMartDatabaseConstants.TABLE_MERCHANTS, null);


        if (c.getCount() > 0) {
            c.moveToFirst();
            String id = c.getString(c.getColumnIndex("MerchantId"));
            String M_name = c.getString(c.getColumnIndex("MerchantName"));
            do {


                bean = new Merchants_against_items();
                bean.setMerchant_name(c.getString(c.getColumnIndex("MerchantName")));
                bean.setMerchant_id(c.getString(c.getColumnIndex("MerchantId")));

                Cursor c1 = db.rawQuery("Select MerchantId,MerchantName,qnty,minqnty,offers,price," +
                        "  Product_name,Freeitemqty,Freeitemname,validfrom,validto from "
                        + AnyMartDatabaseConstants.TABLE_MERCHANTS +
                        " where MerchantId='" + id + "'", null);


                if (c1.getCount() > 0) {
                    c1.moveToFirst();
                    try {
                        do {
                            bean.setQnty(c1.getInt(c1.getColumnIndex("qnty")));
                            bean.setMinqnty(c1.getInt(c1.getColumnIndex("minqnty")));
                            bean.setOffers(c1.getString(c1.getColumnIndex("offers")));
                            bean.setPrice(c1.getFloat(c1.getColumnIndex("price")));
                            bean.setProduct_name(c1.getString(c1.getColumnIndex("Product_name")));
                            bean.setFreeitemqty(c1.getInt(c1.getColumnIndex("Freeitemqty")));
                            bean.setFreeitemname(c1.getString(c1.getColumnIndex("Freeitemname")));
                            bean.setValidfrom(c1.getString(c1.getColumnIndex("validfrom")));
                            bean.setValidto(c1.getString(c1.getColumnIndex("validto")));

                        } while (c1.moveToNext());

                    } finally {

                    }
                }

                arrayList.add(bean);

            } while (c.moveToNext());

        }

       *//* regMarchantList.setAdapter(new AddRegularMerchantAdapter
                (AddRegularMerchants.this, arrayList, this));*//*
    }*/

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Imageviewer thread
    Thread t = new Thread() {
        @Override
        public void run() {
            try {
                while (!isInterrupted()) {
                    Thread.sleep(3500);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                           // iv.setImageResource(imgthumbIds[i]);

                            try{
                                Picasso.with(parent)
                                        //.load("http://prasad.ekatm.com/Images/snacks.png")
                                       // .load(imgthumbIds[i])
                                        .load(bannerDispList.get(i))
                                        .into(iv);

                            }catch (Exception e){
                                e.printStackTrace();
                            }

                            overridePendingTransition(R.anim.enter_right_to_left, R.anim.exit_left_to_right);
                            i++;
                           // if (i >= imgthumbIds.length) {
                            if (i >= bannerDispList.size()) {
                                i = 0;
                            }
                        }
                    });
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    private void hideItem() {
        //navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.location).setVisible(false);
        nav_Menu.findItem(R.id.myinfo).setVisible(false);
        nav_Menu.findItem(R.id.notification).setVisible(false);
    }

    private void initialize() {
        parent = MainActivity.this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);

        //Image slider one after another
        iv = (ImageView) findViewById(R.id.imageview_main);
        i = 0;
        t.start();

        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle(getResources().getString(R.string.home));
        getSupportActionBar().setTitle(Html.fromHtml("<small>"+getResources().getString(R.string.home)+"</small>"));

        selprodcat = findViewById(R.id.selprodcat);
        txtlaunchsoon = findViewById(R.id.txtlaunchsoon);
        txtnoordnote = findViewById(R.id.txtnoordnote);
       // coordinatorLayout = findViewById(R.id.coordinatorLayout_main);
        txtdelto = findViewById(R.id.txtdelto);
        btndeladdr = findViewById(R.id.btndeladdr);
        recentorderlistlayout = (LinearLayout) findViewById(R.id.recentorderlistlayout);
        coach_mark_master_view = findViewById(R.id.coach_mark_master_view);
        btngotit = findViewById(R.id.btngotit);
        lay_category = (LinearLayout)findViewById(R.id.lay_category);
        lay_category.setVisibility(View.GONE);
        listview = findViewById(R.id.listview_home_category_list);
        lay_update_app = findViewById(R.id.lay_update_app);
        edtsearch_product = findViewById(R.id.edtsearch_product);
        pending_ordrcnt = (TextView) findViewById(R.id.pending_ordrcnt);
        listview_recent_ordered_list = findViewById(R.id.listview_recent_ordered_list);
        noorder = (TextView) findViewById(R.id.noorder);
        noorder.setText(""+getResources().getString(R.string.no_ord_place_on));
        noorder.setVisibility(View.VISIBLE);
        startshopping = (LinearLayout) findViewById(R.id.startshopping);
        txt_showmore = (TextView) findViewById(R.id.txt_showmore);
        imageview_goto = (ImageView) findViewById(R.id.imageview_goto);
        //txt_cartcount = (TextView) findViewById(R.id.txt_cartcount);
   //     imgcart =  findViewById(R.id.imgcart);
        txt_lastmerch =  findViewById(R.id.txt_lastmerch);
        plcwith =  findViewById(R.id.plcwith);
        laybtn_bymerch =  findViewById(R.id.laybtn_bymerch);
        txtupdateversion =  findViewById(R.id.txtupdateversion);
        btnsrch_server =  findViewById(R.id.btnsrch_server);
        btnchangemode =  findViewById(R.id.btnchangemode);
        btnupdateversion =  findViewById(R.id.btnupdateversion);
        txtupdateversion.setVisibility(View.INVISIBLE);
        btnupdateversion.setVisibility(View.INVISIBLE);

        try{
            databaseHelper = new DatabaseHelper(parent, AnyMartDatabaseConstants.DATABASE__NAME_URL);
            sql_db = databaseHelper.getWritableDatabase();
        }catch (Exception e){
            e.printStackTrace();
        }

        pending_ordrcnt.setText(String.valueOf(index));

        arrayList = new ArrayList<AllCatSubcatItems>();
        arrayList_subcat = new ArrayList<AllCatSubcatItems>();
        arrayList_bean = new ArrayList<MyCartBean>();
        historyBeanList = new ArrayList<OrderHistoryBean>();
        serchProd = new ArrayList<AllCatSubcatItems>();
        serchProd1 = new ArrayList<AllCatSubcatItems>();
        tempList = new ArrayList<AllCatSubcatItems>();
        serchProdstr = new ArrayList<String>();
        serchProdstr1 = new ArrayList<String>();
        bannerDispList = new ArrayList<String>();
        bannerList = new ArrayList<Banner>();

        URL_list = new ArrayList<URL_Company_Domain>();

       /*old viewpagercode*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //  toggle.setHomeAsUpIndicator(R.drawable.hamburger_1);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().setGroupVisible(R.id.group_1_drawrmenu_one, true);
        navigationView.getMenu().setGroupVisible(R.id.group_2_switchvendor, false);
        navigationView.getMenu().findItem(R.id.communicate).setVisible(true);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        ataaleliurl = AnyMartData.URL;

        if (AnyMartData.MODULE.equalsIgnoreCase("PETRO")) {
           // txtownername.setVisibility(View.VISIBLE);
            hideItem();
        }

        txtusername = (TextView) header.findViewById(R.id.txtusername);
        txtmobileno = (TextView) header.findViewById(R.id.txtmobileno);
        txtappversion = navigationView.findViewById(R.id.appversion);
        txtownername = (TextView) header.findViewById(R.id.txtownername);
        account_view_icon_button = (ToggleButton)header.findViewById(R.id.account_view_icon_button);
        navheader_accountslist = (ListView)header.findViewById(R.id.list_urlnames);
        navheader_accountslist.setVisibility(View.GONE);

        getUrlListFromDataBase();

        dbHandler = new DatabaseHandler(MainActivity.this);

        sharedpreferences = getSharedPreferences(SplashActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        restoredText = sharedpreferences.getString("Mobileno", null);
        AnyMartData.MOBILE = restoredText;
        //restoredownername = sharedpreferences.getString("OwnerName", null);
        restoredusername = sharedpreferences.getString("username", null);
        usertype = sharedpreferences.getString("usertype", null);
        domainname = sharedpreferences.getString("companyURL_LOGO",null);
        restoredownername = sharedpreferences.getString("companyURL_LOGO",null);
        AnyMartData.MAIN_URL = sharedpreferences.getString("CompanyURL",null);
        AnyMartData.CompanyURL = sharedpreferences.getString("companyurlmain",null);
        CustVendorMasterId = sharedpreferences.getString("CustVendorMasterId",null);
        CustomerID = sharedpreferences.getString("CustVendorMasterId",null);
        AnyMartDatabaseConstants.DATABASE__NAME_URL = sharedpreferences.getString("DatabaseName",null);
        AnyMartData.LoginId = sharedpreferences.getString("LoginId",null);
        AnyMartData.Password = sharedpreferences.getString("Password",null);
        AnyMartData.LANGUAGE = sharedpreferences.getString("Language","");
        AnyMartData.AppCode = sharedpreferences.getString("AppCode","");
        AnyMartData.Environment = sharedpreferences.getString("Environment","");
        AnyMartData.MerchantID = sharedpreferences.getString("MerchantID","");
        AnyMartData.MerchantName = sharedpreferences.getString("MerchantName","");
        AnyMartData.SHIPToAddr = sharedpreferences.getString("SHIPToAddr","");
        AnyMartData.SHIPTOMASTERID = sharedpreferences.getString("ShipToId","");
        AnyMartData.LATITUDE = sharedpreferences.getString("Latitude","");
        AnyMartData.LONGITUDE = sharedpreferences.getString("Longitude","");
        AnyMartData.CITY = sharedpreferences.getString("City","");
        AnyMartData.LOCALITY = sharedpreferences.getString("Locality","");
        AnyMartData.PINCODE = sharedpreferences.getString("Pincode","");
        isDelAddrSelected = sharedpreferences.getBoolean("isDelAddrSel",false);
        AnyMartData.ADDRESS = sharedpreferences.getString("Address","");
        AnyMartData.selected_BSEGMENTDESC = sharedpreferences.getString("SelBSegDesc","");
        AnyMartData.selected_BSEGMENTCODE = sharedpreferences.getString("SelBSegCode","");
        AnyMartData.selected_BSEGMENTID = sharedpreferences.getString("SelBSegId","");
        AnyMartData.selected_MERCHID = sharedpreferences.getString("SelMerchId","");
        AnyMartData.SHOPBYMODE = sharedpreferences.getString("SHOPBYMODE","ShopBySpeciality");
        AnyMartData.STATE = sharedpreferences.getString("State","");
        shopByMerchant = sharedpreferences.getBoolean("shopByMerchant",false);
        DirectSegmentSearch = sharedpreferences.getBoolean("DirectSegmentSearch",false);
        AnyMartData.SpecImgPath = sharedpreferences.getString("SpecImgPath","");
        discardmsg_dispcnt = sharedpreferences.getInt("DiscardCart_PopUp",0);
        AnyMartData.Banner = sharedpreferences.getString("BannerList","");
        playstoreflag = sharedpreferences.getInt("playstoreflag",0);
        diayn = sharedpreferences.getString("Dialog", "YesDialog");
        dicurDate = sharedpreferences.getString("TodaysDate", TODAYDATE);
        AnyMartData.instr_custHome_flag = sharedpreferences.getBoolean("custHomeInstr", false);

        txtdelto.setText(""+getResources().getString(R.string.deliverto)+" "+ AnyMartData.ADDRESS);
        isDelAddrSelected = true;

        if(AnyMartData.AppCode.equalsIgnoreCase("SM")){
            account_view_icon_button.setVisibility(View.VISIBLE);
        }else {
            account_view_icon_button.setVisibility(View.INVISIBLE);
        }

       // getCartCount();

        merchants_against_itemsArrayList = new ArrayList<Merchants_against_items>();

        try{
            Intent intent = getIntent();
            if (intent.hasExtra("message")) {
                message = intent.getExtras().getString("message");
                if (message != null) {
                    if (message.contains("confirmed") || message.contains("rejected")) {
                        databaseHelper.deleteNotification();
                        databaseHelper.deleteNotification_rejected();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        if (restoredText != null) {
            Mobilenumber = restoredText;
            AnyMartData.MOBILE = restoredText;
        }
        if (AnyMartData.MODULE.equalsIgnoreCase("PETRO")) {
            /*txtownername.setText("Owner : " + restoredownername);
            txtusername.setText("User : " + restoredusername + " (" + restoredText + ")");*/
            txtmobileno.setVisibility(View.GONE);
        } else if (AnyMartData.MODULE.equalsIgnoreCase("ORDERBILLING")) {
            txtusername.setText(restoredusername);
            txtmobileno.setText(restoredText);
            txtownername.setText(restoredownername);
        }

        try{
            getBannersFromServer();
           /* if(AnyMartData.Banner.equalsIgnoreCase("")){
                getBannersFromServer();
            }else {
                DisplayBanner(bannerDispList);
            }*/
        }catch (Exception e){
            e.printStackTrace();
        }

        PackageManager manager = parent.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(
                    parent.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = info.versionName;
        try {
            txtappversion.setText(getResources().getString(R.string.appversion)+ " "+ version);
        }catch (Exception e){
            e.printStackTrace();
        }

        if(AnyMartData.instr_custHome_flag == false){
            coach_mark_master_view.setVisibility(View.VISIBLE);
        }else {
            coach_mark_master_view.setVisibility(View.GONE);
        }

    }

    private void getUrlListFromDataBase() {

        // TODO Auto-generated method stub
        URL_list.clear();

        db_URLStore = new DatabaseHelper_URLStore(parent);
        sql_db = db_URLStore.getWritableDatabase();

        Cursor c1 = sql_db.rawQuery("Select Url from "
                + AnyMartDatabaseConstants.TABLE_URL_COMPANYDOMAIN, null);
        Log.e("cnt", String.valueOf(c1));
        if(c1.getCount()>0){
            c1.moveToFirst();
            do{
                String urlname = c1.getString(c1.getColumnIndex("Url"));
            }while (c1.moveToNext());
            //url found
        }else {
            //no url found
        }

        Cursor c = sql_db.rawQuery("Select distinct Url, DBName, CustVendorMasterId,EnvMasterId,PlantMasterId,Instance from "
                + AnyMartDatabaseConstants.TABLE_URL_COMPANYDOMAIN, null);
        Log.e("cnt", String.valueOf(c));
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                bean_url = new URL_Company_Domain();
                //bean.setCompanyId(c.getString(c.getColumnIndex("MerchantName")));
                String urlname = c.getString(c.getColumnIndex("Url"));
                String DB_name = c.getString(c.getColumnIndex("DBName"));
                String CustVendorMasterId = c.getString(c.getColumnIndex("CustVendorMasterId"));
                String EnvMasterId = c.getString(c.getColumnIndex("EnvMasterId"));
                String PlantMasterId = c.getString(c.getColumnIndex("PlantMasterId"));
                String instance = c.getString(c.getColumnIndex("Instance"));

                //AnyMartData.EnvMasterId = EnvMasterId;
                //AnyMartData.PlantMasterId = PlantMasterId;
                String usr = "Select FullName,Mobile from "+AnyMartDatabaseConstants.TABLE_USER +" WHERE UserId='"+CustVendorMasterId+"'";
                Cursor cusr = sql_db.rawQuery(usr,null);
                if(cusr.getCount() > 0){
                    cusr.moveToFirst();
                    do{
                        String usrname  = cusr.getString(cusr.getColumnIndex("FullName"));
                        String mob = cusr.getString(cusr.getColumnIndex("Mobile"));

                        bean_url.setUserName(usrname);
                        bean_url.setMobile(mob);
                    }while (cusr.moveToNext());
                }

                bean_url.setUrlname(c.getString(c.getColumnIndex("Url")));
                bean_url.setDBName(c.getString(c.getColumnIndex("DBName")));
                bean_url.setCustVendorMasterId(c.getString(c.getColumnIndex("CustVendorMasterId")));
                bean_url.setEnvMasterId(c.getString(c.getColumnIndex("EnvMasterId")));
                bean_url.setPlantMasterId(c.getString(c.getColumnIndex("PlantMasterId")));
                bean_url.setInstance(instance);
                URL_list.add(bean_url);
                //URL_list.add(urlname);
            } while (c.moveToNext());
        } else {
            /*bean_url = new URL_Company_Domain();
            bean_url.setCompanyId("No data");
            bean_url.setUrlname("No data");
            URL_list.add(bean_url);*/
        }

        urlAdapter = new URL_ListAdapter(parent, URL_list);
        navheader_accountslist.setAdapter(urlAdapter);
        //navheader_accountslist.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        setListViewHeightBasedOnItems(navheader_accountslist);

       /* ArrayAdapter<String> adapter = new ArrayAdapter<String>(parent,
                android.R.layout.simple_list_item_1, URL_list);
        navheader_accountslist.setAdapter(adapter);*/
    }

    private void getDataFromServer() {

        if (NetworkUtils.isNetworkAvailable(parent)) {
            new StartSession(parent, new CallbackInterface() {
                @Override
                public void callMethod() {
                   // new GetCategoryList().execute();

                    new GetMerchFamilyMaster().execute();    //merchant familymaster

                   /* if(shopByMerchant == false){
                         new GetFamilyMasterData().execute();  //allfammmaster
                    }else {
                        new GetMerchFamilyMaster().execute();    //merchant familymaster
                    }*/
                }
                @Override
                public void callfailMethod(String s) {
                }
            });

        } else {
               Toast.makeText(parent, ""+getResources().getString(R.string.nointernet), Toast.LENGTH_LONG).show();
           // callSnackbar();
        }
    }

  /*  public void callSnackbar() {
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, ""+getResources().getString(R.string.nointernet), Snackbar.LENGTH_LONG)
                .setAction("RETRY", new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //new GetCategoryList().execute();
                    }
                });

        // Changing message text color
        snackbar.setActionTextColor(Color.RED);

        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);

        snackbar.show();
    }*/

    @SuppressWarnings({"unchecked", "rawtypes"})
    protected void parseJson(String json) {

        Utilities.clearTable(parent, AnyMartDatabaseConstants.TABLE_ALL_CAT_SUBCAT_ITEMS);
        arrayList.clear();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                //String PricelistId = jsonArray.getJSONObject(i).getString("PricelistId");
                String PricelistId = jsonArray.getJSONObject(i).getString("PricelistHdrId");
                String PricelistRate = jsonArray.getJSONObject(i).getString("PricelistRate");
                String MRP = jsonArray.getJSONObject(i).getString("MRP");

                //http://adtest.ekatm.com/images/87f2d6d9-ce38-4195-8902-c0dce1d67030_coconut_250ml.jpg
                String itemPhoto = jsonArray.getJSONObject(i).getString("ItemPhoto");

                String itmPhotoPath = "";
                if(itemPhoto.equalsIgnoreCase("") || itemPhoto.equalsIgnoreCase(null)){
                    itmPhotoPath = "";
                }else {
                    itmPhotoPath = AnyMartData.CompanyURL+"/images/"+itemPhoto;
                }

                String storeMRP;
                if(PricelistId.equalsIgnoreCase("")){
                    storeMRP = MRP;
                }else {

                    if(PricelistRate.equalsIgnoreCase("0") ||
                            PricelistRate.equalsIgnoreCase("0.0")){
                        storeMRP = MRP;
                    }else {
                        storeMRP = PricelistRate;
                    }
                }
                String itmName = "";
                String name = jsonArray.getJSONObject(i).getString("itemnaame");
                String[] idata = null;

                if(name.contains("- 0.00") || name.contains("- 0.0")){
                    idata  = jsonArray.getJSONObject(i).getString("itemnaame").split("- 0.00");
                    itmName = idata[0];
                }else {
                    //idata doesnot contain -0.00
                    try{
                        idata = jsonArray.getJSONObject(i).getString("itemnaame").split("-");
                        String name1 = idata[0];

                        if(idata.length > 1){
                            String nameuom = idata[1].split("\\.")[0];
                            if(name1.contains(nameuom)){
                                itmName = name1;
                            }else {
                                itmName = name;
                            }
                        }else {
                            itmName = name1;
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                databaseHelper.addAllCatSubcatItems(jsonArray.getJSONObject(i).getString("CategoryId"),
                        jsonArray.getJSONObject(i).getString("CategoryName"),
                        jsonArray.getJSONObject(i).getString("SubCategoryId"),
                        jsonArray.getJSONObject(i).getString("SubCategoryName"),
                        jsonArray.getJSONObject(i).getString("itemmasterid"), itmName,
                        itmPhotoPath, storeMRP,
                        jsonArray.getJSONObject(i).getString("custVendorname"),
                       /* jsonArray.getJSONObject(i).getString("TypeFixedPercent")*/"0",
                        jsonArray.getJSONObject(i).getString("validfrom"),
                        jsonArray.getJSONObject(i).getString("validto"),
                        jsonArray.getJSONObject(i).getString("DisRate"),
                        jsonArray.getJSONObject(i).getString("NetRate"),
                        jsonArray.getJSONObject(i).getString("Freeitemid"),
                        jsonArray.getJSONObject(i).getString("Freeitemqty"),
                        jsonArray.getJSONObject(i).getString("Minqty"),
                        jsonArray.getJSONObject(i).getString("Discratepercent"),
                        jsonArray.getJSONObject(i).getString("DiscrateMRP"),
                        jsonArray.getJSONObject(i).getString("PurDigit"),
                        jsonArray.getJSONObject(i).getString("CustVendorMasterId"),
                        jsonArray.getJSONObject(i).getString("PricelistHdrId"),
                        jsonArray.getJSONObject(i).getString("PricelistRate"),
                        "","","","","","",
                        "","","","",AnyMartData.CatImgPath, AnyMartData.SubCatImgPath,"",
                        "","","","","","",
                        "","","","","","","");
            }

            try{
                progress.dismiss();
            }catch (Exception e){
                e.printStackTrace();
            }

            getDataFromDataBase();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getOpenOrderDataFromDatabase() {
        historyBeanList.clear();

        databaseHelper = new DatabaseHelper(parent, AnyMartDatabaseConstants.DATABASE__NAME_URL);
        SQLiteDatabase sql = databaseHelper.getWritableDatabase();

        Cursor c = sql.rawQuery(
                "SELECT distinct SOHeaderId,ConsigneeName FROM " + AnyMartDatabaseConstants.TABLE_MY_ORDER_HISTORY
                        + " WHERE status NOT IN ('Dispatched', 'Received') ORDER BY DoAck desc ",
                null);
        Log.d("test", "" + c.getCount());

        /*Mobile ='"+ AnyMartData.MOBILE + "'*/

        int ordercnt = 0;
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                String orderid = c.getString(c.getColumnIndex("SOHeaderId"));
                ordercnt = ordercnt + 1;
                OrderHistoryBean historybean = new OrderHistoryBean();
                historybean.setSOHeaderId(orderid);
                historybean.setConsigneeName(c.getString(c.getColumnIndex("ConsigneeName")));
                SQLiteDatabase sql1 = databaseHelper.getWritableDatabase();
               /* Cursor c1 = sql1.rawQuery(
                        "SELECT distinct SODate , NetAmt, DODisptch, DORcvd, status, DoAck FROM "
                                + AnyMartDatabaseConstants.TABLE_MY_ORDER_HISTORY + " WHERE SOHeaderId ='"
                                + orderid + "' ORDER BY date(DoAck) desc ",
                        null);*/

                Cursor c2 = sql1.rawQuery(
                        "SELECT * FROM "
                                + AnyMartDatabaseConstants.TABLE_MY_ORDER_HISTORY + " WHERE SOHeaderId ='"
                                + orderid + "' ORDER BY date(DoAck) desc ",
                        null);

                Log.d("test", "" + c2.getCount());

                float amt = 0;
                if (c2.getCount() > 0) {
                    c2.moveToFirst();
                    do {
                        //   float amtofitem = c1.getFloat(c1.getColumnIndex("Rate"));
                        String o_date = c2.getString(c2.getColumnIndex("SODate"));
                        //   amt = amt + amtofitem;

                        historybean.setSODate(o_date);
                       // historybean.setRate(Float.parseFloat(c2.getString(c2.getColumnIndex("NetAmt"))));
                        historybean.setDoAck(c2.getString(c2.getColumnIndex("DoAck")));
                        historybean.setStatus(c2.getString(c2.getColumnIndex("status")));

                    } while (c2.moveToNext());
                }
                historyBeanList.add(historybean);

            } while (c.moveToNext());
        } else {

        }
        // myOrderHistoryAdapter = new MyOrderHistoryAdapter(MyOrderHistory.this, arrayList);
        // listview_my_orders_history.setAdapter(myOrderHistoryAdapter);
    }

    private void getDataFromDataBase_old() {
        // TODO Auto-generated method stub
        arrayList.clear();

        sql_db = databaseHelper.getWritableDatabase();

        String que = "Select distinct CategoryId, CategoryName,CatImgPath,SubCatCount from "
                + AnyMartDatabaseConstants.TABLE_FAMILY_MASTERDATA +" ORDER BY CategoryName ASC";
        Cursor c = sql_db.rawQuery(que, null);
        Log.d("test", "" + c.getCount());
        if (c.getCount() == 1) {
            c.moveToFirst();

            //if single category open pendingordlist
          /*  listview.setVisibility(View.GONE);
            recentorderlistlayout.setVisibility(View.VISIBLE);
            listview_recent_ordered_list.setVisibility(View.VISIBLE);
            startshopping.setVisibility(View.VISIBLE);
*/

            lay_category.setVisibility(View.VISIBLE);
            listview.setVisibility(View.VISIBLE);
            recentorderlistlayout.setVisibility(View.GONE);
            listview_recent_ordered_list.setVisibility(View.GONE);
            startshopping.setVisibility(View.GONE);
            /////////////////////////////////////////////////////////////////////////////////////

            new StartSession(parent, new CallbackInterface() {

                @Override
                public void callMethod() {
                    //new GetPendingOrderHistoryList().execute();
                }

                @Override
                public void callfailMethod(String s) {

                }
            });

            cat_name = c.getString(c.getColumnIndex("CategoryName"));
            cat_id = c.getString(c.getColumnIndex("CategoryId"));
            CatImgPath = c.getString(c.getColumnIndex("CatImgPath"));
            SubCatCount = c.getInt(c.getColumnIndex("SubCatCount"));

            bean = new AllCatSubcatItems();
            bean.setCategoryId(cat_id);
            bean.setCategoryName(cat_name);
            bean.setCatImgPath(CatImgPath);
            bean.setSubcatcount(SubCatCount);

            arrayList.add(bean);

            if(isDelAddrSelected == false){
                openDialogueDelBox();
            }else {
                txtdelto.setText(""+getResources().getString(R.string.deliverto)+" "+ AnyMartData.ADDRESS);

               /* Intent intent = new Intent(parent, SubCategoryActivity.class);
                intent.putExtra("CategoryName", cat_name);
                intent.putExtra("Category_Id", cat_id);
                intent.putExtra("CustomerID",CustomerID);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);*/
            }

        } else if (c.getCount() > 1) {
            c.moveToFirst();

            lay_category.setVisibility(View.VISIBLE);
            listview.setVisibility(View.VISIBLE);
            recentorderlistlayout.setVisibility(View.GONE);
            listview_recent_ordered_list.setVisibility(View.GONE);
            startshopping.setVisibility(View.GONE);

            try {
                do {
                    int subcatcount = 0;
                    int itemcount = 0;
                    String cat_name = c.getString(c.getColumnIndex("CategoryName"));
                    String cat_id = c.getString(c.getColumnIndex("CategoryId"));
                    CatImgPath = c.getString(c.getColumnIndex("CatImgPath"));
                    SubCatCount = c.getInt(c.getColumnIndex("SubCatCount"));

                    bean = new AllCatSubcatItems();
                    bean.setCategoryId(cat_id);
                    bean.setCategoryName(cat_name);
                    bean.setCatImgPath(CatImgPath);
                   //bean.setSubcatcount(SubCatCount);

                    //old fammastr logic
                    Cursor cursor = sql_db.rawQuery("Select distinct SubCategoryName,SubCategoryId from "
                            + AnyMartDatabaseConstants.TABLE_FAMILY_MASTERDATA + "" +
                            " where CategoryName='" + cat_name + "'", null);
                    Log.d("test", "" + cursor.getCount());
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        try {
                            do {
                                subcatcount = cursor.getCount();
                                String subcat_name = cursor.getString(cursor.getColumnIndex("SubCategoryName"));
                                String subcat_id = cursor.getString(cursor.getColumnIndex("SubCategoryId"));

                                bean.setSubCategoryName(subcat_name);
                                bean.setSubCategoryId(subcat_id);
                                bean.setSubcatcount(subcatcount);
                            }
                            while (cursor.moveToNext());
                        //    cursor.close();

                        } finally {

                        }
                    }

                    arrayList.add(bean);

                } while (c.moveToNext());
            } finally {

            }
        }

        cadt = new CategoryAdapter(parent,arrayList);
        listview.setAdapter(cadt);
        setGridViewHeightBasedOnChildren(listview,3);
    }

    private void getDataFromDataBase() {
        // TODO Auto-generated method stub
        arrayList.clear();

        sql_db = databaseHelper.getWritableDatabase();

        String que = "Select distinct CategoryId, CategoryName,CatImgPath,SubCatCount from "
                + AnyMartDatabaseConstants.TABLE_FAMILY_MASTERDATA +" ORDER BY CategoryName ASC";
        Cursor c = sql_db.rawQuery(que, null);
        Log.d("test", "" + c.getCount());
        if (c.getCount() == 1) {
            c.moveToFirst();

            //if single category open pendingordlist
          /*  listview.setVisibility(View.GONE);
            recentorderlistlayout.setVisibility(View.VISIBLE);
            listview_recent_ordered_list.setVisibility(View.VISIBLE);
            startshopping.setVisibility(View.VISIBLE);
*/

            lay_category.setVisibility(View.VISIBLE);
            listview.setVisibility(View.VISIBLE);
            recentorderlistlayout.setVisibility(View.GONE);
            listview_recent_ordered_list.setVisibility(View.GONE);
            startshopping.setVisibility(View.GONE);
            /////////////////////////////////////////////////////////////////////////////////////

                new StartSession(parent, new CallbackInterface() {

                    @Override
                    public void callMethod() {
                        //new GetPendingOrderHistoryList().execute();
                    }

                    @Override
                    public void callfailMethod(String s) {

                    }
                });

            cat_name = c.getString(c.getColumnIndex("CategoryName"));
            cat_id = c.getString(c.getColumnIndex("CategoryId"));
            CatImgPath = c.getString(c.getColumnIndex("CatImgPath"));
            SubCatCount = c.getInt(c.getColumnIndex("SubCatCount"));

            bean = new AllCatSubcatItems();
            bean.setCategoryId(cat_id);
            bean.setCategoryName(cat_name);
            bean.setCatImgPath(CatImgPath);
            bean.setSubcatcount(SubCatCount);
            bean.setBusiSegImgPath(AnyMartData.SpecImgPath);

            arrayList.add(bean);

            if(isDelAddrSelected == false){
                openDialogueDelBox();
            }else {
                txtdelto.setText(""+getResources().getString(R.string.deliverto)+" "+ AnyMartData.ADDRESS);

                //call API and open itemlist directly
                selectedCatid = cat_id;
                selectedCatName = cat_name;
                SubCatCount = SubCatCount;

                if (NetworkUtils.isNetworkAvailable(parent)) {
                    new StartSession(parent, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new GetMerchFamilyMaster_subcategory().execute();
                        }

                        @Override
                        public void callfailMethod(String s) {

                        }
                    });
                }

               /* Intent intent = new Intent(parent, SubCategoryActivity.class);
                intent.putExtra("CategoryName", cat_name);
                intent.putExtra("Category_Id", cat_id);
                intent.putExtra("CustomerID",CustomerID);
                intent.putExtra("SubCatCount",String.valueOf(SubCatCount));
                intent.putExtra("CatImgPath",CatImgPath);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);*/

            }

        } else if (c.getCount() > 1) {
            c.moveToFirst();

            lay_category.setVisibility(View.VISIBLE);
            listview.setVisibility(View.VISIBLE);
            recentorderlistlayout.setVisibility(View.GONE);
            listview_recent_ordered_list.setVisibility(View.GONE);
            startshopping.setVisibility(View.GONE);

            try {
                do {
                    int subcatcount = 0;
                    int itemcount = 0;
                    String cat_name = c.getString(c.getColumnIndex("CategoryName"));
                    String cat_id = c.getString(c.getColumnIndex("CategoryId"));
                    CatImgPath = c.getString(c.getColumnIndex("CatImgPath"));
                    SubCatCount = c.getInt(c.getColumnIndex("SubCatCount"));

                    bean = new AllCatSubcatItems();
                    bean.setCategoryId(cat_id);
                    bean.setCategoryName(cat_name);
                    bean.setCatImgPath(CatImgPath);
                    bean.setSubcatcount(SubCatCount);
                    bean.setBusiSegImgPath(AnyMartData.SpecImgPath);

                    //old fammastr logic
                   /* Cursor cursor = sql_db.rawQuery("Select distinct SubCategoryName,SubCategoryId from "
                            + AnyMartDatabaseConstants.TABLE_FAMILY_MASTERDATA + "" +
                            " where CategoryName='" + cat_name + "'", null);
                    Log.d("test", "" + cursor.getCount());
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        try {
                            do {
                                subcatcount = cursor.getCount();
                                String subcat_name = cursor.getString(cursor.getColumnIndex("SubCategoryName"));
                                String subcat_id = cursor.getString(cursor.getColumnIndex("SubCategoryId"));

                                bean.setSubCategoryName(subcat_name);
                                bean.setSubCategoryId(subcat_id);
                                bean.setSubcatcount(subcatcount);
                            }
                            while (cursor.moveToNext());
                            cursor.close();

                        } finally {

                        }
                    }*/

                    arrayList.add(bean);

                } while (c.moveToNext());
            } finally {

            }
        }

        cadt = new CategoryAdapter(parent,arrayList);
        listview.setAdapter(cadt);
        setGridViewHeightBasedOnChildren(listview,3);
    }

    private void setListeners() {

        btngotit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                AnyMartData.instr_custHome_flag = true;

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean("custHomeInstr", AnyMartData.instr_custHome_flag);
                editor.commit();

                coach_mark_master_view.setVisibility(View.GONE);
            }
        });

        btnsrch_server.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = edtsearch_product.getText().toString().trim();
                if(txt == "" || txt == null){

                }else {
                    getItemFromServer(txt);
                }
            }
        });

        edtsearch_product.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try{
                    String selProduct="",selProdId="",selItmImgPath="",selBrand="";

                    //if array string adapter used
                    /*for(int i=0; i<serchProd.size();i++){
                        if(edtsearch_product.getText().toString().equalsIgnoreCase(serchProd.get(i).getItemName())){
                            selProduct = serchProd.get(i).getItemName();
                            selProdId = serchProd.get(i).getItemMasterId();
                            selItmImgPath = serchProd.get(i).getItemImgPath();
                        }
                    }*/

                    //if custom adapter used
                    selProduct = tempList.get(position).getItemName();
                    selBrand = tempList.get(position).getBrand();
                    selProduct = tempList.get(position).getItemName();
                    selProdId = tempList.get(position).getItemMasterId();
                    selItmImgPath = tempList.get(position).getItemImgPath();

                    edtsearch_product.setText(selBrand + " "+ selProduct);

                    //send to ItemMultimerch screen
                    Intent intent = new Intent(MainActivity.this, Multimerchant_ProductListActivity.class);
                    intent.putExtra("ItemMasterID",selProdId);
                    intent.putExtra("ItemDesc",selProduct);
                    intent.putExtra("ItemImgPath",selItmImgPath);
                    intent.putExtra("KeyCall","Search_ItemId");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        edtsearch_product.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                edtsearch_product.showDropDown();
                return false;
            }
        });

        btnchangemode.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,BusinessSegmentListActivity.class);
                intent.putExtra("callFrom","HomeChangeMode");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        laybtn_bymerch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Intent intent = new Intent(MainActivity.this, MerchantSelectionActivity.class);
                intent.putExtra("CustomerID",CustomerID);
                intent.putExtra("BSegmentCode",AnyMartData.selected_BSEGMENTCODE);
                intent.putExtra("BSegmentId",AnyMartData.selected_BSEGMENTID);
                intent.putExtra("callFrom","Speciality");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);*/

                if(isDelAddrSelected == false){
                    openDialogueDelBox();
                }else {
                    txtdelto.setText(""+getResources().getString(R.string.deliverto)+" "+ AnyMartData.ADDRESS);

                    Intent intent = new Intent(MainActivity.this, MerchantSelectionActivity.class);
                    intent.putExtra("CustomerID",CustomerID);
                    intent.putExtra("BSegmentCode",AnyMartData.selected_BSEGMENTCODE);
                    intent.putExtra("BSegmentId",AnyMartData.selected_BSEGMENTID);
                    intent.putExtra("callFrom","Speciality");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });

        btndeladdr.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                openDialogueDelBox();
            }
        });

        btnupdateversion.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=com.vritti.freshmart"));
                startActivity(intent);

                //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.vritti.freshmart")));

                //checkForAppUpdate();
            }
        });

   /*     imgcart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isDelAddrSelected == false){
                    openDialogueDelBox();
                }else {
                    txtdelto.setText(""+getResources().getString(R.string.deliverto)+" "+ AnyMartData.ADDRESS);
                    Intent intent = new Intent(parent,WishlistActivity.class);
                    startActivity(intent);
                }
            }
        });*/

        account_view_icon_button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                    //account_view_icon_button.setBackgroundResource(R.drawable.up_triangle);
                    navheader_accountslist.setVisibility(View.VISIBLE);
                    navigationView.getMenu().setGroupVisible(R.id.group_1_drawrmenu_one, false);
                    navigationView.getMenu().setGroupVisible(R.id.group_2_switchvendor, true);
                    navigationView.getMenu().findItem(R.id.communicate).setVisible(false);
                   // Toast.makeText(MainActivity.this, "clicked", Toast.LENGTH_SHORT).show();
                }else {
                    //account_view_icon_button.setBackgroundResource(R.drawable.down_triangle);

                    navheader_accountslist.setVisibility(View.GONE);
                    navigationView.getMenu().setGroupVisible(R.id.group_1_drawrmenu_one, true);
                    navigationView.getMenu().setGroupVisible(R.id.group_2_switchvendor, false);
                    navigationView.getMenu().findItem(R.id.communicate).setVisible(true);
                }

                // mAdapter.setUseAccountMode(isChecked);
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                String selectedItem = arrayList.get(position).getCategoryName();

                /*Snackbar snackbar = Snackbar.make(
                        lay_category,
                        "Selected : " + selectedItem,
                        Snackbar.LENGTH_LONG
                );

                snackbar.getView().setBackgroundColor(Color.parseColor("#FF66729B"));
                snackbar.show();*/

                // Initialize a new color drawable array
                int mGridViewBGColor = Color.parseColor("#87b2d3");
                ColorDrawable[] colors = {
                        new ColorDrawable(mGridViewBGColor), // Animation starting color
                        new ColorDrawable(mGridViewBGColor) // Animation ending color
                };

                // Initialize a new transition drawable instance
                TransitionDrawable transitionDrawable = new TransitionDrawable(colors);

                // Set the clicked item background
                view.setBackground(transitionDrawable);

                // Finally, Run the item background color animation
                // This is the grid view item click effect
                transitionDrawable.startTransition(100); //600 Milliseconds

                AnyMartData.CatImgPath = arrayList.get(position).getCatImgPath();
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("CatImgPath", AnyMartData.CatImgPath);
                editor.commit();

                if(isDelAddrSelected == false){
                    openDialogueDelBox();
                }else {
                    txtdelto.setText(""+getResources().getString(R.string.deliverto)+" "+ AnyMartData.ADDRESS);

                    if(arrayList.get(position).getSubcatcount() == 1){
                        //call API and open itemlist directly
                        selectedCatid = arrayList.get(position).getCategoryId();
                        selectedCatName = arrayList.get(position).getCategoryName();
                        SubCatCount = arrayList.get(position).getSubcatcount();

                        if (NetworkUtils.isNetworkAvailable(parent)) {
                            new StartSession(parent, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new GetMerchFamilyMaster_subcategory().execute();
                                }

                                @Override
                                public void callfailMethod(String s) {

                                }
                            });
                        }
                    }else {

                        AnyMartData.selectedCategoryName = arrayList.get(position).getCategoryName();
                        Intent intent = new Intent(parent, SubCategoryActivity.class);
                        intent.putExtra("CategoryName", arrayList.get(position).getCategoryName());
                        intent.putExtra("Category_Id", arrayList.get(position).getCategoryId());
                        intent.putExtra("CustomerID",CustomerID);
                        intent.putExtra("SubCatCount",String.valueOf(SubCatCount));
                        intent.putExtra("CatImgPath",CatImgPath);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(R.anim.enter_right_to_left, R.anim.exit_left_to_right);
                    }
                }
            }
        });

        navheader_accountslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url_position_name = URL_list.get(position).getUrlname();
                String instance = URL_list.get(position).getInstance();
                String db_position = URL_list.get(position).getDBName();
                String db_custmastid = URL_list.get(position).getCustVendorMasterId();
                String envMasterId = URL_list.get(position).getEnvMasterId();
                String plantMasterId = URL_list.get(position).getPlantMasterId();
                restoredusername = URL_list.get(position).getUserName();
                restoredText = URL_list.get(position).getMobile();
                AnyMartData.EnvMasterId = envMasterId;
                AnyMartData.PlantMasterId = plantMasterId;
                CustomerID = db_custmastid;
                AnyMartData.LoginId = "";
                AnyMartData.Password = "";

                if (url_position_name != null || url_position_name != "") {

                    CopmanyURL = "http://" + url_position_name + instance+"/api/OrderBillingAPI/";
                }
                //CompanyURL = "http://"+enterurl.getText().toString()+"/api/OrderBillingAPI/";

                AnyMartData.CompanyURL = "http://" + url_position_name+instance;
                AnyMartData.MAIN_URL = CopmanyURL;
                AnyMartDatabaseConstants.DATABASE__NAME_URL = db_position;
                restoredownername = url_position_name;
               // AnyMartData.SESSION_ID = null;
               // AnyMartData.HANDLE = null;

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("CompanyURL", AnyMartData.MAIN_URL);
                editor.putString("companyurlmain", AnyMartData.CompanyURL);
                editor.putString("companyURL_LOGO",url_position_name);
                editor.putString("AppEnvMasterId",AnyMartData.EnvMasterId);
                editor.putString("PlantMasterId",AnyMartData.PlantMasterId);
                editor.putString("DatabaseName", AnyMartDatabaseConstants.DATABASE__NAME_URL);
                editor.putString("CustVendorMasterId", CustomerID);
                editor.putString("username", restoredusername);
                editor.putString("Mobileno", restoredText);
                editor.putString("companyURL_LOGO", restoredownername);
                editor.putString("logopath", "");
                editor.commit();

                txtusername.setText(restoredusername);
                txtmobileno.setText(restoredText);
                txtownername.setText(restoredownername);

                databaseHelper = new DatabaseHelper(MainActivity.this, AnyMartDatabaseConstants.DATABASE__NAME_URL);

                Toast.makeText(MainActivity.this,""+parent.getResources().getString(R.string.switch_shopcode),Toast.LENGTH_SHORT).show();

                recreate();
            }
        });

        listview_recent_ordered_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> p, View view, final int position, long id) {

                if(isDelAddrSelected == false){
                    openDialogueDelBox();
                }else {
                    txtdelto.setText(""+getResources().getString(R.string.deliverto)+" "+ AnyMartData.ADDRESS);

                    try{
                        RefreshOrderHistory refreshOrderHistory=new RefreshOrderHistory();
                        refreshOrderHistory.RefreshOrderHistoryData(MainActivity.this);

                        String SOHeaderID = historyBeanList.get(position).getSOHeaderId();
                        String status = historyBeanList.get(position).getStatus();
                        String statusname = historyBeanList.get(position).getStatusname();
                        String TotalAmt = String.valueOf(historyBeanList.get(position).getNetAmt());
                        String sono = historyBeanList.get(position).getSONo();
                        String address = historyBeanList.get(position).getAddress();

                        Intent intent_go = new Intent(parent, OrderDetailsActivity.class);
                        intent_go.putExtra("SOHeaderID", SOHeaderID);
                        intent_go.putExtra("status", status);
                        intent_go.putExtra("statusname",/*statusname*/"Created");
                        intent_go.putExtra("TotalAmt",TotalAmt);
                        intent_go.putExtra("OrderNumber",sono);
                        intent_go.putExtra("DelvryAddress",address);
                        startActivity(intent_go);
                        overridePendingTransition(R.anim.enter_right_to_left, R.anim.exit_left_to_right);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }
        });

        txt_showmore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                index = historyBeanList.size() + 10;
                // Toast.makeText(parent,"index = "+index,Toast.LENGTH_SHORT ).show();
                    new StartSession(parent, new CallbackInterface() {

                        @Override
                        public void callMethod() {
                            new GetPendingOrderHistoryList().execute();
                        }

                        @Override
                        public void callfailMethod(String s) {

                        }
                    });
            }
        });

        startshopping.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDelAddrSelected == false){
                    openDialogueDelBox();
                }else {
                    txtdelto.setText(""+getResources().getString(R.string.deliverto)+" "+ AnyMartData.ADDRESS);

                    Intent intent_goto = new Intent(parent, SubCategoryActivity.class);
                    intent_goto.putExtra("CategoryName", cat_name);
                    intent_goto.putExtra("Category_Id", cat_id);
                    intent_goto.putExtra("CustomerID",CustomerID);
                    overridePendingTransition(R.anim.enter_right_to_left, R.anim.exit_left_to_right);
                    intent_goto.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent_goto);
                }
            }
        });

        edtsearch_product.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try{

                    //custom adapter
                   /* try{
                        if(s.toString().equalsIgnoreCase("") || s.toString().equalsIgnoreCase(null)){

                        }else {
                           adt.getFilter(s.toString().trim());
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }*/

             //       filter_serch(edtsearch_product.getText().toString());        //if ArrayAdapter

                       /* if(count > 0){
                            //go to product list and display all products

                            if(AnyMartData.AppCode.equalsIgnoreCase("SM")||
                                    // || AnyMartData.selected_MERCHID != "" || AnyMartData.selected_MERCHID != null
                            shopByMerchant == true && AnyMartData.SHOPBYMODE.equalsIgnoreCase("ShopByMerchant")){
                                Intent intent = new Intent(parent, ItemListActivity.class);
                                intent.putExtra("CustomerID",CustomerID);
                               // intent.putExtra("PurDigit",PurDigit);
                                intent.putExtra("CallType","DirectSearch");
                                intent.putExtra("SubCategoryId",subCatId_directserch);
                                intent.putExtra("ItemKey",edtsearch_product.getText().toString().trim());
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                overridePendingTransition(R.anim.enter_right_to_left,R.anim.exit_left_to_right);
                                //finish();
                            }else{
                                Intent intent = new Intent(parent, ItemListActivity_Multimerchant.class);
                                intent.putExtra("CustomerID",CustomerID);
                                //intent.putExtra("PurDigit",PurDigit);
                                intent.putExtra("CallType","DirectSearch");
                                intent.putExtra("SubCategoryId",subCatId_directserch);
                                intent.putExtra("ItemKey",edtsearch_product.getText().toString().trim());
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                overridePendingTransition(R.anim.enter_right_to_left,R.anim.exit_left_to_right);
                                //finish();
                            }

                        }else {
                            Toast.makeText(parent,""+getResources().getString(R.string.nomatchfound),Toast.LENGTH_LONG).show();
                        }*/
                //    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public class GetFamilyMasterData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String responseString = "";
        String response_list = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try{
                progress = ProgressHUD.show(parent,
                        ""+getResources().getString(R.string.loading_cat), false, true, null);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            //old familymasterdata all families
            String url_getCategory_List = AnyMartData.MAIN_URL + AnyMartData.METHOD_GET_FAMILY_MASTER +
                    "?handler=" + AnyMartData.HANDLE +
                    "&sessionid=" + AnyMartData.SESSION_ID;
            //"&MerchId=&BSegmentid=" + AnyMartData.selected_BSEGMENTID;

            String getcatSubItems = url_getCategory_List;
            URLConnection urlConnection = null;
            BufferedReader bufferedReader = null;
            try {

                res = Utility.OpenconnectionOrferbilling(url_getCategory_List, parent);
                int a = res.getBytes().length;
                res = res.replaceAll("\\\\", "");
                // res = res.replaceAll("\"", "");
                // res = res.replaceAll(" ", "");
                responseString = res.toString().replaceAll("^\"|\"$", "");
                Log.e("Response", responseString);

                /*responseString = stringBuff_getItems.toString().replaceAll("^\"|\"$", "");*/
                response_list = responseString.replaceAll("\\\\", "");
                System.out.println("resp =" + response_list);

            } catch (Exception e) {
                response_list = "error";
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try{
                if (response_list.equalsIgnoreCase("Session Expired")) {
                    if (NetworkUtils.isNetworkAvailable(parent)) {
                        new StartSession(parent, new CallbackInterface() {
                            @Override
                            public void callMethod() {

                                new GetFamilyMasterData().execute();
                            }

                            @Override
                            public void callfailMethod(String s) {

                            }
                        });
                    }
                } else if (response_list.equalsIgnoreCase("error")) {
                    Toast.makeText(parent, ""+parent.getResources().getString(R.string.servererror), Toast.LENGTH_LONG).show();
                } else {
                    json = response_list;
                    parseJson_FamilyMaster(json);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    protected void parseJson_FamilyMaster(String json) {

       // Utilities.clearTable(parent, AnyMartDatabaseConstants.TABLE_ALL_CAT_SUBCAT_ITEMS);
        Utilities.clearTable(parent, AnyMartDatabaseConstants.TABLE_FAMILY_MASTERDATA);
        arrayList.clear();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {

                String catImgPath = "",SubcatImgPath = "";

                catImgPath  =  jsonArray.getJSONObject(i).getString("CatImgPath");
                 SubcatImgPath =  jsonArray.getJSONObject(i).getString("SubCatImgPath");

                if(catImgPath.equalsIgnoreCase("") || catImgPath.equalsIgnoreCase(null)){
                    catImgPath = "";
                }else {
                    catImgPath = AnyMartData.CompanyURL+"/images/"+catImgPath;
                }

                if(SubcatImgPath.equalsIgnoreCase("") || SubcatImgPath.equalsIgnoreCase(null)){
                    SubcatImgPath = "";
                }else {
                    SubcatImgPath = AnyMartData.CompanyURL+"/images/"+SubcatImgPath;
                }
                databaseHelper.addFamilyMaster(jsonArray.getJSONObject(i).getString("CategoryId"),
                        jsonArray.getJSONObject(i).getString("CategoryName"),
                        jsonArray.getJSONObject(i).getString("SubCategoryId"),
                        jsonArray.getJSONObject(i).getString("SubCategoryName"),
                        "",catImgPath,SubcatImgPath,"","");
            }

            try{
                progress.dismiss();
            }catch (Exception e){
                e.printStackTrace();
            }

            getDataFromDataBase_old();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class GetMerchFamilyMaster extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String responseString = "";
        String response_list = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try{
                progress = ProgressHUD.show(parent,
                        ""+getResources().getString(R.string.loading_cat), false, true, null);
                progress.setCanceledOnTouchOutside(true);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            //old familymasterdata all families
            String url_getCategory_List = "";

            if(AnyMartData.SHOPBYMODE.equalsIgnoreCase("ShopBySpeciality")){
                url_getCategory_List = AnyMartData.MAIN_URL + AnyMartData.METHOD_GET_FAMILY_MASTER_MERCHANT +
                        "?handler=" + AnyMartData.HANDLE +
                        "&sessionid=" + AnyMartData.SESSION_ID+
                        "&MerchantId=&type=Category&BusSegmentId=" + AnyMartData.selected_BSEGMENTID+"&CategoryId=";
            }else {
                url_getCategory_List = AnyMartData.MAIN_URL + AnyMartData.METHOD_GET_FAMILY_MASTER_MERCHANT +
                        "?handler=" + AnyMartData.HANDLE +
                        "&sessionid=" + AnyMartData.SESSION_ID+
                        "&MerchantId=" + AnyMartData.selected_MERCHID+
                        "&type=Category&BusSegmentId=" + AnyMartData.selected_BSEGMENTID+"&CategoryId=";
            }

            String getcatSubItems = url_getCategory_List;
            URLConnection urlConnection = null;
            BufferedReader bufferedReader = null;
            try {

                res = Utility.OpenconnectionOrferbilling(url_getCategory_List, parent);
                int a = res.getBytes().length;
                res = res.replaceAll("\\\\", "");
                // res = res.replaceAll("\"", "");
                // res = res.replaceAll(" ", "");
                responseString = res.toString().replaceAll("^\"|\"$", "");
                Log.e("Response", responseString);

                /*responseString = stringBuff_getItems.toString().replaceAll("^\"|\"$", "");*/
                response_list = responseString.replaceAll("\\\\", "");
                System.out.println("resp =" + response_list);

            } catch (Exception e) {
                response_list = "error";
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try{
                progress.dismiss();
            }catch (Exception e){
                e.printStackTrace();
            }

            try{
                if (response_list.equalsIgnoreCase("Session Expired")) {
                    if (NetworkUtils.isNetworkAvailable(parent)) {
                        new StartSession(parent, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new GetMerchFamilyMaster().execute();
                            }

                            @Override
                            public void callfailMethod(String s) {

                            }
                        });
                    }
                } else if (response_list.equalsIgnoreCase("error")) {
                    Toast.makeText(parent, ""+parent.getResources().getString(R.string.servererror), Toast.LENGTH_LONG).show();
                    txtnoordnote.setVisibility(View.VISIBLE);
                    txtlaunchsoon.setText(getResources().getString(R.string.thanks_for_interes)+" "+AnyMartData.selected_BSEGMENTDESC +" "+
                            getResources().getString(R.string.soon));
                } else if (response_list.equalsIgnoreCase("[]")) {
                    txtnoordnote.setVisibility(View.VISIBLE);
                    txtlaunchsoon.setText(getResources().getString(R.string.thanks_for_interes)+" "+AnyMartData.selected_BSEGMENTDESC +" "+
                            getResources().getString(R.string.soon));
                } else if (response_list.contains("sesEndTime")) {
                    txtnoordnote.setVisibility(View.VISIBLE);
                    txtlaunchsoon.setText(getResources().getString(R.string.thanks_for_interes)+" "+AnyMartData.selected_BSEGMENTDESC +" "+
                            getResources().getString(R.string.soon));
                } else {
                    txtnoordnote.setVisibility(View.GONE);
                    json = response_list;
                    parseJson_MerchFamilyMaster(json);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    protected void parseJson_MerchFamilyMaster(String json) {

        // Utilities.clearTable(parent, AnyMartDatabaseConstants.TABLE_ALL_CAT_SUBCAT_ITEMS);
        Utilities.clearTable(parent, AnyMartDatabaseConstants.TABLE_FAMILY_MASTERDATA);
        arrayList.clear();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {

                String catImgPath =  jsonArray.getJSONObject(i).getString("CatImgPath");
                AnyMartData.CatImgPath = catImgPath;
                /*SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("CatImgPath", AnyMartData.CatImgPath);
                editor.commit();*/

                if(catImgPath.equalsIgnoreCase("") || catImgPath.equalsIgnoreCase(null)){
                    catImgPath = "";
                }else {
                    catImgPath = AnyMartData.CompanyURL+"/images/"+catImgPath;
                }

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("CatImgPath", AnyMartData.CatImgPath);
                editor.commit();

                databaseHelper.addFamilyMaster(jsonArray.getJSONObject(i).getString("CategoryId"),
                        jsonArray.getJSONObject(i).getString("CategoryName"),
                        "","",
                        "",catImgPath,"",
                        jsonArray.getJSONObject(i).getString("SubCategoryCount"),"");
            }

            /*try{
                progress.dismiss();
            }catch (Exception e){
                e.printStackTrace();
            }*/

            getDataFromDataBase();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        try{
            cadt = new CategoryAdapter(parent,arrayList);
            listview.setAdapter(cadt);
            setGridViewHeightBasedOnChildren(listview,3);
            cadt.notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
        }

        sharedpreferences = getSharedPreferences(SplashActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        restoredText = sharedpreferences.getString("Mobileno", null);
        AnyMartData.MOBILE = restoredText;
        //restoredownername = sharedpreferences.getString("OwnerName", null);
        restoredusername = sharedpreferences.getString("username", null);
        usertype = sharedpreferences.getString("usertype", null);
        domainname = sharedpreferences.getString("companyURL_LOGO",null);
        restoredownername = sharedpreferences.getString("companyURL_LOGO",null);
        AnyMartData.MAIN_URL = sharedpreferences.getString("CompanyURL",null);
        AnyMartData.CompanyURL = sharedpreferences.getString("companyurlmain",null);
        CustVendorMasterId = sharedpreferences.getString("CustVendorMasterId",null);
        CustomerID = sharedpreferences.getString("CustVendorMasterId",null);
        AnyMartDatabaseConstants.DATABASE__NAME_URL = sharedpreferences.getString("DatabaseName",null);
        AnyMartData.LoginId = sharedpreferences.getString("LoginId",null);
        AnyMartData.Password = sharedpreferences.getString("Password",null);
        AnyMartData.LANGUAGE = sharedpreferences.getString("Language","");
        AnyMartData.AppCode = sharedpreferences.getString("AppCode","");
        AnyMartData.Environment = sharedpreferences.getString("Environment","");
        AnyMartData.MerchantID = sharedpreferences.getString("MerchantID","");
        AnyMartData.MerchantName = sharedpreferences.getString("MerchantName","");
        AnyMartData.SHIPToAddr = sharedpreferences.getString("SHIPToAddr","");
        AnyMartData.SHIPTOMASTERID = sharedpreferences.getString("ShipToId","");
        AnyMartData.LATITUDE = sharedpreferences.getString("Latitude","");
        AnyMartData.LONGITUDE = sharedpreferences.getString("Longitude","");
        AnyMartData.CITY = sharedpreferences.getString("City","");
        AnyMartData.LOCALITY = sharedpreferences.getString("Locality","");
        AnyMartData.PINCODE = sharedpreferences.getString("Pincode","");
        isDelAddrSelected = sharedpreferences.getBoolean("isDelAddrSel",false);
        AnyMartData.ADDRESS = sharedpreferences.getString("Address","");
        AnyMartData.selected_BSEGMENTCODE = sharedpreferences.getString("SelBSegCode","");
        AnyMartData.selected_BSEGMENTID = sharedpreferences.getString("SelBSegId","");
        AnyMartData.selected_MERCHID = sharedpreferences.getString("SelMerchId","");
        AnyMartData.selected_BSEGMENTDESC = sharedpreferences.getString("SelBSegDesc","");
        AnyMartData.Banner = sharedpreferences.getString("BannerList","");
        shopByMerchant = sharedpreferences.getBoolean("shopByMerchant",false);
        DirectSegmentSearch = sharedpreferences.getBoolean("DirectSegmentSearch",false);

        txtdelto.setText(""+getResources().getString(R.string.deliverto)+" "+ AnyMartData.ADDRESS);

        edtsearch_product.setText("");

        getDataFromShipToData();

        getLastOrdPlaceWith();

        try{

            getBannersFromServer();
            /*if(AnyMartData.Banner.equalsIgnoreCase("")){
                getBannersFromServer();
            }else {
                DisplayBanner(bannerDispList);
            }*/
        }catch (Exception e){
            e.printStackTrace();
        }

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        //ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this,imgthumbIds);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this,bannerDispList);
        viewPager.setAdapter(viewPagerAdapter);

        try{
            getCartCount();
        }catch (Exception e){
            e.printStackTrace();
        }

        if (databaseHelper.getAllCatSubcatItemCount(this) > 0) {
            checkItemInDatabase("");
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MainActivity.this, BusinessSegmentListActivity.class);
        intent.putExtra("callFrom","HomeScreenFirstCall");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

        /*if(isDelAddrSelected == false){

        }else {
            ++backpressCount;
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                backpressCount = 0;
                drawer.closeDrawer(GravityCompat.START);
            } else {

                if (backpressCount == 1) {
                    //do whatever you want to do on first click for example:
                    Toast.makeText(this, ""+getResources().getString(R.string.clickbacktoast), Toast.LENGTH_SHORT).show();
                    back_pressed = System.currentTimeMillis();
                } else if (backpressCount == 2) {
                    finish();
                }
            }
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_refresh, menu);
        final MenuItem menuItem = menu.findItem(R.id.cart);
        final MenuItem menuItemrefresh = menu.findItem(R.id.refresh);
        menuItemrefresh.setVisible(false);
        MenuItemCompat.setActionView(menuItem, update_count);
        RelativeLayout countLayout = (RelativeLayout)MenuItemCompat.getActionView(menuItem);
        txt_cartcount = (TextView)countLayout.findViewById(R.id.txt_cartcount);

        countLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent,WishlistActivity.class);
                startActivity(intent);
            }
        });

        getCartCount();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.cart:

                break;

            case R.id.refresh:
                //startService(new Intent(parent, OrderHistoryRefreshService.class));
                RefreshOrderHistory refreshOrderHistory=new RefreshOrderHistory();
                refreshOrderHistory.RefreshOrderHistoryData(MainActivity.this);

                if (NetworkUtils.isNetworkAvailable(parent)) {
                 new StartSession(parent, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                //new GetCategoryList().execute();
                                new GetFamilyMasterData().execute();

                            }

                            @Override
                            public void callfailMethod(String s) {

                            }
                        });
                } else {
                       Toast.makeText(parent, ""+getResources().getString(R.string.nointernet), Toast.LENGTH_LONG).show();
                    //callSnackbar();
                }

                return true;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateHotCount(final int new_hot_number) {
        hot_number = new_hot_number;
        if (ct_count.getText().toString().equalsIgnoreCase(null)) return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (new_hot_number == 0)
                    ct_count.setVisibility(View.INVISIBLE);
                else {
                    ct_count.setVisibility(View.VISIBLE);
                    ct_count.setText(Integer.toString(new_hot_number));
                }
            }
        });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        /*if(id == R.id.vendorname){

            Intent intent1 = new Intent(getApplicationContext(), CompanyDomainList.class);
            startActivity(intent1);

        }else*/ if(id == R.id.addaccount){
            Intent intent1 = new Intent(getApplicationContext(), CompanyUrlActivity.class);
            startActivity(intent1);

        }else if (id == R.id.location) {
            Intent intent1 = new Intent(getApplicationContext(), GpsLocationActivity.class);
            startActivity(intent1);

        } else if (id == R.id.myinfo) {

            Intent intent1 = new Intent(getApplicationContext(), UserProfileUpdateActivity.class);
            startActivity(intent1);

        } else if (id == R.id.history) {
            //Intent intent = new Intent(getApplicationContext(), MyOrderHistory.class);
            Intent intent = new Intent(getApplicationContext(), MyOrderHistory_Tabactivity.class);
            intent.putExtra("appName","C");
            startActivity(intent);

        } else if (id == R.id.nav_share_customer) {

            try {
                Intent i = new Intent(android.content.Intent.ACTION_SEND);
                i.setType("text/plain"); /*image/*,*/
               // i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra(Intent.EXTRA_SUBJECT, "Any Dukaan" /*AnyMartData.MODULE + "App"*/);
                String msg = "\n Let me recommend you Any Dukaan application\n\n";
                String url1= "<a href= 'https://play.google.com/store/apps/details?id=com.vritti.freshmart'>https://play.google.com/store/apps/details?id=com.vritti.freshmart</a>";
                Uri linkurl = Uri.parse(url1);
                imageUri = Uri.parse("android.resource://" + getPackageName()+ "/drawable/" + "logo121_anymart");
                i.putExtra(Intent.EXTRA_TEXT, msg + "\n" +Html.fromHtml(url1));
                //i.putExtra(Intent.EXTRA_STREAM, imageUri);
                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(i, "Choose one to Share link!"));

            } catch (Exception e) {
                e.printStackTrace();
            }

        } /*else if (id == R.id.notification) {
            Intent intent1 = new Intent(com.vritti.freshmart.customer.MainActivity.this, NotificationActivity.class);
            startActivity(intent1);

        }*//*else if(id == R.id.mycart){
           Intent intent = new Intent(getApplicationContext(),WishlistActivity.class);
           startActivity(intent);

        } *//*else if (id == R.id.regular_merchant) {

            if (databaseHelper.getMerchantsAgainstItems() > 1) {
                //if more than one merchant is in listc
                Intent intent = new Intent(getApplicationContext(), RegularMarchantActivity.class);
                startActivity(intent);
            } else {
                showNewPrompt();
               *//* Toast.makeText(getApplicationContext(),
                        "Only one merchant is present, No need to select merchant", Toast.LENGTH_LONG).show();*//*
            }
        }*/ /*else if (id == R.id.nav_help) {
            //go to help activity
            Intent intent = new Intent(getApplicationContext(), HelpActivity.class);
            startActivity(intent);
        }*/else if (id == R.id.language) {
            //language change logic
            //open dialogue box
            openDialogueBox();
        }else {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(getApplicationContext(), ""+getResources().getString(R.string.searchresult) + query, Toast.LENGTH_LONG).show();
            mySearch(query);
        }
    }

    protected void mySearch(String query) {

        for (AllCatSubcatItems s : arrayList) {
            if (s.getCategoryName().contains(query)) {
                arrayList.add(s);
            }
        }
    }

    public void setLocale(String localeName) {
        if (!localeName.equals(currentLanguage)) {
            myLocale = new Locale(localeName);
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);

            AnyMartData.LANGUAGE = localeName;
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("Language", AnyMartData.LANGUAGE);
            editor.commit();

            recreate();

            /*Intent refresh = new Intent(this, MainActivity.class);
            refresh.putExtra(currentLang, localeName);
            startActivity(refresh);*/
        } else {
            Toast.makeText(MainActivity.this, ""+getResources().getString(R.string.lang_already_selected), Toast.LENGTH_SHORT).show();
        }
    }

    private void shareIt() {
//sharing implementation here
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Any Mart");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "\n Now order products with Any Mart application, " +
                "\n \t click here to visit \n\n https://play.google.com/store/apps/details?id=com.vritti.freshmart");
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public void showNewPrompt() {
        final Dialog myDialog = new Dialog(parent);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(dialog_freq_merchant);
        myDialog.setCancelable(true);

        //myDialog.getWindow().setGravity(Gravity.BOTTOM);
        databaseHelper = new DatabaseHelper(parent, AnyMartDatabaseConstants.DATABASE__NAME_URL);
        SQLiteDatabase sql_db = databaseHelper.getWritableDatabase();

        Cursor c_merchantName = sql_db.rawQuery("select MerchantName from " + AnyMartDatabaseConstants.TABLE_MERCHANTS, null);
        Log.d("test", "" + c_merchantName.getCount());

        if (c_merchantName.getCount() > 0) {
            c_merchantName.moveToFirst();
        }

        final TextView quest = (TextView) myDialog.findViewById(R.id.textMsg);
        quest.setText("Only one merchant is present, No need to add it in Frequent Merchant_MM List");
        Button btnyes = (Button) myDialog
                .findViewById(R.id.btn_ok);
        btnyes.setText("OK");
        btnyes.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
               /* Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);/*
                finish();*/
               myDialog.dismiss();
            }
        });

        myDialog.show();
    }

    public void showItemDetails(String itemid) {
        final Dialog myDialog = new Dialog(parent);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(dialog_display_details);
        myDialog.setCancelable(true);

        //myDialog.getWindow().setGravity(Gravity.BOTTOM);
        databaseHelper = new DatabaseHelper(parent, AnyMartDatabaseConstants.DATABASE__NAME_URL);
        SQLiteDatabase sql_db = databaseHelper.getWritableDatabase();

        final TextView quest = (TextView) myDialog.findViewById(R.id.textdetails);
        final TextView txtname = (TextView) myDialog.findViewById(R.id.txtname);
        final TextView txtrate = (TextView) myDialog.findViewById(R.id.txtrate);
        final TextView txtqty = (TextView) myDialog.findViewById(R.id.txtqty);
        final TextView txtlineamt = (TextView) myDialog.findViewById(R.id.txtlineamt);
        final TextView txtorderdate = (TextView) myDialog.findViewById(R.id.txtorderdate);
        final TextView txtdeliverydate = (TextView) myDialog.findViewById(R.id.txtdeliverydate);

        String orderID = null;
        Cursor c_recentorder = sql_db.rawQuery("select distinct SOHeaderId from " + AnyMartDatabaseConstants.TABLE_MY_ORDER_HISTORY + " ORDER BY DoAck DESC", null);
        Log.d("test", "" + c_recentorder.getCount());
        if (c_recentorder.getCount() > 0) {
            c_recentorder.moveToFirst();
            orderID = c_recentorder.getString(c_recentorder.getColumnIndex("SOHeaderId"));
        }

        Cursor c_orderdetails = sql_db.rawQuery("select * from " + AnyMartDatabaseConstants.TABLE_MY_ORDER_HISTORY + " where SOHeaderId='" + orderID + "' AND ItemMasterId='" + itemid + "'", null);
        Log.d("test", "" + c_orderdetails.getCount());

        String itemName = null;
        String itemID = null;
        Float Qty = null;
        Float Amount = null;
        Float Rate = null;
        String Merchant_Name = null;
        String Merchant_id = null;
        String DoAck = null;
        String SODate = null;

        if (c_orderdetails.getCount() > 0) {
            c_orderdetails.moveToFirst();
            do {
                itemName = c_orderdetails.getString(c_orderdetails.getColumnIndex("ItemDesc"));
                itemID = c_orderdetails.getString(c_orderdetails.getColumnIndex("ItemMasterId"));
                Qty = Float.valueOf(c_orderdetails.getString(c_orderdetails.getColumnIndex("Qty")));
                Amount = Float.valueOf(c_orderdetails.getString(c_orderdetails.getColumnIndex("LineAmt")));
                Rate = Float.valueOf(c_orderdetails.getString(c_orderdetails.getColumnIndex("Rate")));
                Merchant_Name = c_orderdetails.getString(c_orderdetails.getColumnIndex("merchantname"));
                Merchant_id = c_orderdetails.getString(c_orderdetails.getColumnIndex("merchantid"));
                DoAck = c_orderdetails.getString(c_orderdetails.getColumnIndex("DoAck"));
                SODate = c_orderdetails.getString(c_orderdetails.getColumnIndex("SODate"));

                //SimpleDateFormat Format = new SimpleDateFormat("MMM dd yyyy");//Feb 23 2016 12:16PM
                SimpleDateFormat Format = new SimpleDateFormat("dd MMM yyyy");//23 Feb 2016 12:16PM
                //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
                SimpleDateFormat toFormat = new SimpleDateFormat("MM/dd/yyyy");
                Date d1 = null;
                Date d2_ack = null;
                try {
                    d1 = format.parse(SODate);
                    d2_ack = format.parse(DoAck);
                    //DateToStr = toFormat.format(date);
                    DateToString = Format.format(d1);
                    DateToString_ack = Format.format(d2_ack);
                    // DateToStr = format.format(d1);
                    System.out.println(DateToString);
                    System.out.println(DateToString_ack);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            } while (c_orderdetails.moveToNext());
        }
        quest.setText("Item Details");
        txtname.setText(itemName);
        txtrate.setText(Rate + " ₹");
        txtqty.setText((String.valueOf(Qty)));
        txtlineamt.setText(Amount + " ₹");
        //txtorderdate.setText(DateToStr);
        txtorderdate.setText(DateToString_ack);
        txtdeliverydate.setText(DateToString);

        Button btnyes = (Button) myDialog
                .findViewById(R.id.btntxtok);
        btnyes.setText("OK");
        btnyes.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        myDialog.show();
    }

    public void showNoDataInWishlist() {
        final Dialog myDialog = new Dialog(parent);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(dialog_ok);
        myDialog.setCancelable(true);

        //myDialog.getWindow().setGravity(Gravity.BOTTOM);

        final TextView quest = (TextView) myDialog.findViewById(R.id.textMsg);
        quest.setText("Sorry! No items in Wishlist Cart");

        final Button SelectDate = (Button) myDialog
                .findViewById(R.id.btn_selectdate_ok);
        SelectDate.setVisibility(View.GONE);

        final Button btnok = (Button) myDialog.findViewById(R.id.copy_btnok);
        btnok.setText("Ok");

        btnok.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.show();
    }

    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
        }

    }

    private void callforplayStore() {
        String PlayStoreVersion = null;
        String MyAppVersion = null;
        if(NetworkUtils.isNetworkAvailable(MainActivity.this)) {
            try {
                MyAppVersion = (getPackageManager().getPackageInfo(getPackageName(), 0).versionName);

                Document doc = Jsoup.connect("https://play.google.com/store/apps/details?id="
                        + "com.vritti.freshmart").get();
                String AllStr = doc.text();
                String parts[] = AllStr.split("Current Version");
                String newparts[] = parts[1].split("Requires Android");
                PlayStoreVersion = newparts[0].trim();

                if(!MyAppVersion.equals(PlayStoreVersion)){
                    if(dialogopen.equalsIgnoreCase("no")) {

                        Date date = new Date();
                        final Calendar c = Calendar.getInstance();

                        Year = c.get(Calendar.YEAR);
                        month = c.get(Calendar.MONTH);
                        day = c.get(Calendar.DAY_OF_MONTH);

                        TODAYDATE = day + "-" + (month + 1) + "-" + Year;

                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        //editor.putString("Dialog", "NoDialog");
                        editor.putString("TodaysDate",TODAYDATE);
                        editor.apply();

                        showUpdateDialog(PlayStoreVersion);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();

            }catch (NullPointerException e){
                e.printStackTrace();

            }catch (Exception e){
                e.printStackTrace();

            }
        }
    }

    private void showUpdateDialog(String PSVersion) {
        try {
           // txtupdateversion.setVisibility(View.VISIBLE);
           // btnupdateversion.setVisibility(View.VISIBLE);

            /*Animation hrtbeat = AnimationUtils.loadAnimation(this, R.anim.heartbeat);
            btnupdateversion.startAnimation(hrtbeat);*/
           /* String updatemsg = " "+getResources().getString(R.string.new_version)+" " + PSVersion + " " +getResources().getString(R.string.is_on_playstore)+" \n"
                    +getResources().getString(R.string.toaccess);
            txtupdateversion.setText(updatemsg);*/

            sharedpreferences = getSharedPreferences(SplashActivity.MyPREFERENCES,Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();

            final Dialog myDialog = new Dialog(parent);
            myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            myDialog.setContentView(app_update_lay);
            myDialog.setCancelable(false);

            final TextView txtupdateversion = myDialog.findViewById(R.id.txtupdateversion);
            final Button btnupdateversion = myDialog.findViewById(R.id.btnupdateversion);
            final Button btncancel = myDialog.findViewById(R.id.btncancel);

            String updatemsg = " "+getResources().getString(R.string.new_version)+" "
                    +Html.fromHtml("<b>"+PSVersion+"</b>")+" "
                    +getResources().getString(R.string.is_on_playstore)+" \n"
                    +getResources().getString(R.string.toaccess);
            txtupdateversion.setText(updatemsg);

            btnupdateversion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=com.vritti.freshmart"));
                    startActivity(intent);

                    dialogopen = "no";
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("Dialog", "NoDialog");
                    //editor.putString("TodaysDate",TODAYDATE);
                    editor.apply();
                    myDialog.dismiss();
                }
            });

            btncancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialogopen = "no";
                    myDialog.dismiss();
                }
            });

            dialogopen = "yes";
            myDialog.show();

           /* final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("New Update Available!");
            builder.setMessage("New Any Mart version " + PSVersion + " is on Playstore."
                    "(Note: In playstore 'OPEN' button is visible instead of 'UPDATE', Uninstall and Install app)");

            builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.vritti.freshmart")));
                    //dialogopen = "no";
                    dialog.dismiss();
                }
            });

            builder.setNegativeButton("No, Thanks", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //background.start();
                    //dialogopen = "no";
                    dialog.dismiss();
                }
            });

            builder.setCancelable(false);

            dialog = builder.show();*/
            //dialogopen = "yes";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getCartCount(){
        int count = 0;
            count = databaseHelper.getCartItems();
            txt_cartcount.setText(String.valueOf(count));

            //show dialog msg

        /*if(count > 0){

            dialog = new Dialog(parent);
            dialog.setContentView(R.layout.dialog_message);
            TextView txtMsg = (TextView) dialog.findViewById(R.id.textMsg);
            Button btnyes = (Button) dialog.findViewById(R.id.btn_yes);
            Button btnno = (Button) dialog.findViewById(R.id.btn_no);
            EditText edtreason =  dialog.findViewById(R.id.edtreason);
            edtreason.setVisibility(View.GONE);
            txtMsg.setText(getResources().getString(R.string.dilg_discardcart));
            dialog.show();

            btnyes.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Utilities.clearTable(parent, AnyMartDatabaseConstants.TABLE_CART_ITEM);
                    Toast.makeText(parent,""+getResources().getString(R.string.cart_discarded),Toast.LENGTH_LONG).show();

                    int count = 0;
                    count = databaseHelper.getCartItems();
                    txt_cartcount.setText(String.valueOf(count));

                    dialog.dismiss();
                }
            });

            btnno.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }*/
    }

    public class ViewPagerAdapter extends PagerAdapter{
        private Context context;
        private LayoutInflater layoutInflater;
        //int images[];
        //String images[];
        ArrayList<String> images = new ArrayList<String>();
        //private Integer [] images = {R.drawable.slide1,R.drawable.slide2,R.drawable.slide3};

        public ViewPagerAdapter(Context context, ArrayList<String> images) {
            this.context = context;
            this.images = images;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public ViewPagerAdapter(MainActivity context, String[] imgthumbIds) {
            this.context = context;
            //this.images = imgthumbIds;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view.equals(object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.viewpager_item,container, false);

            assert view != null;

            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            //imageView.setImageResource(R.drawable.banner_orange);

            String img = bannerDispList.get(position);

            try{
                Picasso.with(parent)
                        .load(img)
                        .into(imageView);
            }catch (Exception e){
                e.printStackTrace();
            }

            imageView.setVisibility(View.VISIBLE);
            videoView = view.findViewById(R.id.videoview);
            videoView.setVisibility(View.GONE);

            //play video
            /*VideoView videoHolder = new VideoView(MainActivity.this);
            videoHolder.setMediaController(new MediaController(MainActivity.this));
            Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.baneer_vid1);
            pathVideo = "android.resource://" + getPackageName() + "/" + R.raw.baneer_vid1;
            videoView.setVideoURI(Uri.parse(pathVideo));
            videoView.start();*/

            container.addView(view);

            /*ViewPager vp = (ViewPager) container;
            vp.addView(view, 0);*/
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            /*ViewPager vp = (ViewPager) container;
            View view = (View) object;
            vp.removeView(view);*/
            container.removeView((RelativeLayout) object);

        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }
    }

    public int checkItemInDatabase(String itemname){
        if(serchProd.size()>0){
            serchProd.clear();
            serchProdstr.clear();
        }

        int count = 0;
       // String qry = "Select distinct itemmasterid,* from "+AnyMartDatabaseConstants.TABLE_ALL_CAT_SUBCAT_ITEMS+" WHERE ItemName like '%"+itemname+"%'";
        String qry = "Select distinct itemmasterid,* from "+AnyMartDatabaseConstants.TABLE_ALL_CAT_SUBCAT_ITEMS;
        Cursor c = sql_db.rawQuery(qry,null);
        if(c.getCount() > 0){
            c.moveToFirst();
            do{
                count = c.getCount();
                subCatId_directserch = c.getString(c.getColumnIndex("SubCategoryId"));

                AllCatSubcatItems serchitems = new AllCatSubcatItems();
                serchitems.setItemName(c.getString(c.getColumnIndex("ItemName")));
                serchitems.setItemMasterId(c.getString(c.getColumnIndex("itemmasterid")));
                serchitems.setItemImgPath(c.getString(c.getColumnIndex("ItemImgPath")));
                serchitems.setBrand(c.getString(c.getColumnIndex("Brand")));

                String product = c.getString(c.getColumnIndex("Brand"))+" - "+c.getString(c.getColumnIndex("ItemName"));

                serchProd.add(serchitems);
                serchProd1.add(serchitems);
               /* serchProdstr.add(c.getString(c.getColumnIndex("ItemName")));
                serchProdstr1.add(c.getString(c.getColumnIndex("ItemName")));*/
                serchProdstr.add(product);
                serchProdstr1.add(product);

            }while (c.moveToNext());

            /*search_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,serchProdstr);
            edtsearch_product.setAdapter(search_adapter);*/

            adt = new CustomListAdapter(parent,R.layout.custom_search,serchProd);
            edtsearch_product.setAdapter(adt);
        }
        return count;
    }

    public void openDialogueBox(){
        final BottomSheetDialog btmsheetdialog = new BottomSheetDialog(parent);
        View sheetview = getLayoutInflater().inflate(R.layout.dialogue_select_language, null);
        btmsheetdialog.setContentView(sheetview);
        btmsheetdialog.show();
        btmsheetdialog.setCancelable(true);
        btmsheetdialog.setCanceledOnTouchOutside(false);

        RadioGroup radgrp =  btmsheetdialog.findViewById(R.id.radgrp_lang);
        final RadioButton radbtnhindi =  btmsheetdialog.findViewById(R.id.radbtnhindi);
        final RadioButton radbtnenglish =  btmsheetdialog.findViewById(R.id.radbtnenglish);
        final RadioButton radbtnmarathi =  btmsheetdialog.findViewById(R.id.radbtnmarathi);

        radbtnmarathi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    radbtnhindi.setChecked(true);
                    radbtnenglish.setChecked(false);
                    Toast.makeText(parent,""+getResources().getString(R.string.changelanguage),Toast.LENGTH_SHORT).show();
                    Utility.setLocale("mr",MainActivity.this);

                    recreate();

                    btmsheetdialog.dismiss();

                }else {
                }
            }
        });

        radbtnhindi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    radbtnhindi.setChecked(true);
                    radbtnenglish.setChecked(false);
                    Toast.makeText(parent,""+getResources().getString(R.string.changelanguage),Toast.LENGTH_SHORT).show();
                    Utility.setLocale("hi",MainActivity.this);

                    recreate();

                    btmsheetdialog.dismiss();

                }else {
                }
            }
        });

        radbtnenglish.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    radbtnenglish.setChecked(true);
                    radbtnhindi.setChecked(false);
                    Toast.makeText(parent,""+getResources().getString(R.string.changelanguage),Toast.LENGTH_SHORT).show();
                    Utility.setLocale("en",MainActivity.this);

                    recreate();

                    btmsheetdialog.dismiss();

                }else {
                }
            }
        });

    }

    public int getShiptolist(){
        sql_db = databaseHelper.getWritableDatabase();
        String qry = "select * from "+AnyMartDatabaseConstants.TABLE_SHIPTO_DETAILS;
        Cursor c = sql_db.rawQuery(qry,null);
        if(c.getCount() > 0){
            c.moveToFirst();
            return c.getCount();

        }else {
            return 0;
        }
    }

    class DownloadShipToDataJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        JSONArray jResults = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = AnyMartData.CompanyURL + AnyMartData.api_getMultipleShipToData + "?CustId="+ AnyMartData.USER_ID;
            try {
                res = Utility.OpenconnectionOrferbilling(url,parent);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("^\"|\"+$","");
                    //response = response.substring(1, response.length() - 1);
                    jResults = new JSONArray(response);
                }
            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }
            return response;
        }

        @Override
        protected void onPostExecute(String  integer) {
            super.onPostExecute(integer);
            //   dismissProgressDialog();

            try{
                if (jResults != null) {
                    //parse json
                    parseShipToJson(jResults);

                }else {
                    Toast.makeText(parent,"No data", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void parseShipToJson(JSONArray jResults){

        Utilities.clearTable(parent, AnyMartDatabaseConstants.TABLE_SHIPTO_DETAILS);

        String RowID="", Action="", ConsigneeName="", ContactPerson="", Address="", City = "",Phone="", Fax="", Mobile="", Country="",
                State="", ShipToMasterId = "",Latitude="", Longitude="", Distance="", RouteMasterId="", CityName="", CountryName,
                StateName="", GeoLocation="", GSTCode="", GSTState="", GSTStateName="", Rating = "",TANNo="", TANNoName="", PAN="",
                EmailId="", isBlocked="", TAN_GSTIN_Number="",Locality = "";

        for(int i = 0; i<= jResults.length(); i++){
            try {
                JSONObject jObj_shipto = jResults.getJSONObject(i);

                RowID = jObj_shipto.getString("RowID");
                Action = jObj_shipto.getString("Action");
                ConsigneeName = jObj_shipto.getString("ConsigneeName");
                ContactPerson = jObj_shipto.getString("ContactPerson");
                Address = jObj_shipto.getString("Address");
                City = jObj_shipto.getString("City");
                Phone = jObj_shipto.getString("Phone");
                Fax = jObj_shipto.getString("Fax");
                Mobile = jObj_shipto.getString("Mobile");
                Country = jObj_shipto.getString("Country");
                State = jObj_shipto.getString("State");
                ShipToMasterId = jObj_shipto.getString("ShipToMasterId");
                Latitude = jObj_shipto.getString("Latitude");
                Longitude = jObj_shipto.getString("Longitude");
                Distance = jObj_shipto.getString("Distance");
                RouteMasterId = jObj_shipto.getString("RouteMasterId");
                CityName = jObj_shipto.getString("CityName");
                CityName = jObj_shipto.getString("CityName");
                CountryName = jObj_shipto.getString("CountryName");
                StateName = jObj_shipto.getString("StateName");
                GeoLocation = jObj_shipto.getString("GeoLocation");
                GSTCode = jObj_shipto.getString("GSTCode");
                GSTState = jObj_shipto.getString("GSTState");
                GSTStateName = jObj_shipto.getString("GSTStateName");
                Rating = jObj_shipto.getString("Rating");
                TANNo = jObj_shipto.getString("TANNo");
                TANNoName = jObj_shipto.getString("TANNoName");
                PAN = jObj_shipto.getString("PAN");
                EmailId = jObj_shipto.getString("EmailId");
                isBlocked = jObj_shipto.getString("isBlocked");
                TAN_GSTIN_Number = jObj_shipto.getString("TAN_GSTIN_Number");

                AnyMartData.SHIPTOMASTERID = ShipToMasterId;

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("ShipToId", AnyMartData.SHIPTOMASTERID );
                editor.commit();

                String City_state_pin_Country;
                if(CityName.equalsIgnoreCase("") || CityName.equalsIgnoreCase(null)){
                    City_state_pin_Country = City + " " + StateName + "" + CountryName;
                }else {
                    City_state_pin_Country = CityName + " " + StateName + "" + CountryName;
                }
                //String City_state_pin_Country = City + " " + StateName + "" + CountryName;

                Customer cust = new Customer();
                cust.setShipToAddress(Address);
                cust.setLatitude(Latitude);
                cust.setLongitude(Longitude);
                cust.setCity_state_pin_Country( City_state_pin_Country);
                cust.setShiptomasterid(ShipToMasterId);

                //lstReference.add(cust);

                databaseHelper.insertShipTo(RowID, Action, ConsigneeName, ContactPerson, Address, City, Phone, Fax, Mobile, Country,
                        State, ShipToMasterId, Latitude, Longitude, Distance, RouteMasterId, CityName, CountryName, StateName,
                        GeoLocation, GSTCode, GSTState,
                        GSTStateName, Rating,TANNo, TANNoName, PAN, EmailId, isBlocked, TAN_GSTIN_Number,Locality);

            } catch (JSONException e) {
                e.printStackTrace();
                //Toast.makeText(parent,"Data not inserted",Toast.LENGTH_SHORT).show();
            }

            getDataFromShipToData();

        }
    }

    public void openDialogueDelBox(){
        btmsheetdialog_addr = new BottomSheetDialog(parent);
        View sheetview = getLayoutInflater().inflate(dialogue_select_deliveryaddr, null);
        btmsheetdialog_addr.setContentView(sheetview);
        btmsheetdialog_addr.show();
        btmsheetdialog_addr.setCancelable(true);
        btmsheetdialog_addr.setCanceledOnTouchOutside(false);

        Button btnadadr = btmsheetdialog_addr.findViewById(R.id.btnadadr);
        casts_container = btmsheetdialog_addr.findViewById(R.id.casts_container);
        rvlist = btmsheetdialog_addr.findViewById(R.id.rvlist);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvlist.setLayoutManager(mLayoutManager);

        getDataFromShipToData();

        shrv = new ShipToRecyclerAdapter(addrlist,MainActivity.this);
        rvlist.setAdapter(shrv);

        btnadadr.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, GetNewaddress.class);
                intent.putExtra("type","Add");
                intent.putExtra("callFrom","Home");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                btmsheetdialog_addr.dismiss();
            }
        });

        //addviewDelAddr();
    }

    public void getDataFromShipToData(){
        addrlist.clear();

        sql_db = databaseHelper.getWritableDatabase();

        try{
            String qry = "Select Address,Latitude,Longitude,GeoLocation,City,Locality,GUID,State,StateName,ShipToMasterId from "
                    +AnyMartDatabaseConstants.TABLE_SHIPTO_DETAILS;
            Cursor c = sql_db.rawQuery(qry,null);
            if(c.getCount() > 0){
                c.moveToFirst();
                do{
                    String Address = "",Latitude="",Longitude="",area="",CityName = "",Locality = "", addrGUID = "",
                            State = "",ShipToMasterId="";

                    try{
                        Address = c.getString(c.getColumnIndex("Address"));
                        Latitude = c.getString(c.getColumnIndex("Latitude"));
                        Longitude = c.getString(c.getColumnIndex("Longitude"));
                        //String ShipToMasterId = c.getString(c.getColumnIndex("ShipToMasterId"));
                        area = c.getString(c.getColumnIndex("GeoLocation"));
                        CityName = c.getString(c.getColumnIndex("City"));
                        Locality = c.getString(c.getColumnIndex("Locality"));
                        addrGUID = c.getString(c.getColumnIndex("GUID"));
                        State = c.getString(c.getColumnIndex("State"));
                        ShipToMasterId = c.getString(c.getColumnIndex("ShipToMasterId"));
                        if(State.equalsIgnoreCase("")){
                            State = c.getString(c.getColumnIndex("StateName"));
                        }else {
                            State = c.getString(c.getColumnIndex("State"));
                        }

                        if(Locality.equalsIgnoreCase("") || Locality.equalsIgnoreCase(null) ||
                                Locality.equalsIgnoreCase("null")){
                            //get locality from geocordinates
                            getAddressViaGPSCordinates(Latitude,Longitude,addrGUID);
                            /*String[] locAddr = Address.split(CityName)[0].split(" ");
                            int l1 = locAddr.length;
                            area= locAddr[l1-1];*/

                      //      AnyMartData.LOCALITY = area;      //testpurpose commented
                        }else {
                      //      AnyMartData.LOCALITY = Locality;      //testpurpose commented
                        }

                        /*AnyMartData.CITY = CityName;
                        AnyMartData.ADDRESS = Address;
                        AnyMartData.STATE = State;*/

                      /*  SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("Locality", AnyMartData.LOCALITY);
                        editor.putString("City",AnyMartData.CITY);
                        editor.putString("Address",  AnyMartData.ADDRESS);
                        //editor.putString("State",  AnyMartData.STATE);
                        editor.commit();*/

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    Customer cust = new Customer();
                    cust.setShipToAddress(Address);
                    cust.setLatitude(Latitude);
                    cust.setLongitude(Longitude);
                    //cust.setShiptomasterid(ShipToMasterId);
                    cust.setCity_state_pin_Country(area);
                    cust.setCity(CityName);
                    cust.setLocality(Locality);
                    cust.setAddrGUID(addrGUID);
                    cust.setShiptomasterid(ShipToMasterId);

                    addrlist.add(cust);

                }while (c.moveToNext());

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void addviewDelAddr() {
        LayoutInflater inflater = (LayoutInflater) parent.getSystemService(LAYOUT_INFLATER_SERVICE);
        final View baseView = inflater.inflate(R.layout.clickablelay, null);
        int size = addrlist.size();
        for (int i = 0; i < size; i++) {
            Customer cast = addrlist.get(i);
        // create dynamic LinearLayout and set Image on it.
            if (cast != null) {
                clickableColumn = (LinearLayout) inflater.inflate(clickablelay, null);
                //titleView = (TextView) baseView.findViewById(R.id.txtdeladdr);
                titleView = (TextView) clickableColumn.findViewById(R.id.txtdeladdr);
                //thumbnailImage.setImageResource(R.drawable.ic_launcher);

                titleView.setText(addrlist.get(i).getShipToAddress());
                casts_container.addView(clickableColumn);
            }
        }
    }

    public void editSelectedAddress(int position){

        try{
            String addr = "",mainaddr = "",city = "", locality = "",landmark = "",houseno = "",area_colony = "",state = "",pincode = "";
            String lat = "", lng = "", addrGUID = "",shipToId="";
            try{
                index = position;
                addr = addrlist.get(index).getCity_state_pin_Country();
                mainaddr = addrlist.get(index).getShipToAddress();
                city = addrlist.get(index).getCity();
                locality = addrlist.get(index).getLocality();
                //landmark = addrlist.get(index)
                lat = addrlist.get(index).getLatitude();
                lng = addrlist.get(index).getLongitude();
                addrGUID = addrlist.get(index).getAddrGUID();
                shipToId = addrlist.get(index).getShiptomasterid();
                pincode=AnyMartData.PINCODE;

            }catch (Exception e){
                e.printStackTrace();
            }

            String addToSplit = mainaddr.replace(city,"");

            if(addToSplit.contains(AnyMartData.STATE)){
                addToSplit= addToSplit.replace(AnyMartData.STATE,"");
            }else {
                addToSplit= addToSplit.replace(AnyMartData.STATE.toUpperCase(),"");
            }

            addToSplit = addToSplit.replace(AnyMartData.PINCODE,"");
            addToSplit = addToSplit.replace(locality,"");
            addToSplit = addToSplit.replace(",","");

            String addToPutditBox = city+"_"+locality+"_"+AnyMartData.STATE+"_"+AnyMartData.PINCODE+"_"+addToSplit;

            Intent intent = new Intent(MainActivity.this, GetNewaddress.class);
            intent.putExtra("Latitude",lat);
            intent.putExtra("Longitude",lng);
            intent.putExtra("type","Edit");
            intent.putExtra("callFrom","Home");
            intent.putExtra("addrGUID",addrGUID);
            intent.putExtra("addrToSplit",addToPutditBox);
            intent.putExtra("shipToId",shipToId);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            btmsheetdialog_addr.dismiss();
            // titleView.setBackground(getResources().getDrawable(R.drawable.edittext_border_2));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getDelAddress(int position,String addr1){

        try{
            String addr = "",mainaddr = "",city = "", locality = "",landmark = "",state = "",ShipToId="";
            String lat="",lng="";
            try{
                index = position;
                addr = addrlist.get(index).getCity_state_pin_Country();
                mainaddr = addrlist.get(index).getShipToAddress();
                city = addrlist.get(index).getCity();
                locality = addrlist.get(index).getLocality();
                ShipToId = addrlist.get(index).getShiptomasterid();
                lat = addrlist.get(index).getLatitude();
                lng = addrlist.get(index).getLongitude();
                isDelAddrSelected = true;

            }catch (Exception e){
                e.printStackTrace();
                mainaddr = addr1;
                isDelAddrSelected = true;
            }

            if(locality.equalsIgnoreCase("") || locality.equalsIgnoreCase("null") ||
                    locality.equalsIgnoreCase(null)){
                if(addr.equalsIgnoreCase("") || addr.equalsIgnoreCase(null)){
                    AnyMartData.LOCALITY = city;
                    addr = locality + " "+city;
                }else {
                    AnyMartData.LOCALITY = addr;
                }

                // locality = city;

            }else {
                AnyMartData.LOCALITY = locality;
            }

            AnyMartData.CITY = city;
            //AnyMartData.LOCALITY = locality;
            AnyMartData.ADDRESS = mainaddr;
            AnyMartData.SHIPTOMASTERID = ShipToId;
            AnyMartData.LATITUDE=lat;
            AnyMartData.LONGITUDE=lng;

            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("Locality", AnyMartData.LOCALITY);
            editor.putString("City",AnyMartData.CITY);
            editor.putString("Pincode",  AnyMartData.PINCODE);
            editor.putString("Address",  AnyMartData.ADDRESS);
            editor.putBoolean("isDelAddrSel", true);
            editor.putString("ShipToId", AnyMartData.SHIPTOMASTERID);
            editor.putString("Latitude", AnyMartData.LATITUDE);
            editor.putString("Longitude", AnyMartData.LONGITUDE);
            editor.commit();

            txtdelto.setText(""+getResources().getString(R.string.deliverto)+" "+ mainaddr);

            btmsheetdialog_addr.dismiss();
        }catch (Exception e){
            e.printStackTrace();
        }
       // titleView.setBackground(getResources().getDrawable(R.drawable.edittext_border_2));
    }

    public int getPendingOrdCnt(){
        int cnt = 0;
        String qry = "Select SOHeaderId from "+AnyMartDatabaseConstants.TABLE_MY_ORDER_HISTORY+" WHERE status=10";
        Cursor c = sql_db.rawQuery(qry,null);
        if(c.getCount()>0){
            c.moveToFirst();
            cnt =  c.getCount();

        }else {
            return 0;
        }

        return cnt;
    }

    public static void getpndingOrdData() {
        historyBeanList.clear();

        SQLiteDatabase sql = databaseHelper.getWritableDatabase();

        Cursor c = sql.rawQuery(
                "SELECT DISTINCT SOHeaderId, sono, ConsigneeName  FROM " +
                        AnyMartDatabaseConstants.TABLE_MY_ORDER_HISTORY + " WHERE Mobile ='"
                        + AnyMartData.MOBILE + "' AND status='10' ORDER BY sono desc ",
                null);
        /*AND status='10'*/

        int ordercnt = 0;
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                String orderid = c.getString(c.getColumnIndex("SOHeaderId"));
                ordercnt = ordercnt + 1;
                OrderHistoryBean bean = new OrderHistoryBean();

                bean.setSOHeaderId/*(Integer.parseInt*/(orderid); //String.valueOf(ordercnt)
                bean.setSONo(c.getString(c.getColumnIndex("sono")));
                bean.setConsigneeName(c.getString(c.getColumnIndex("ConsigneeName")));

                SQLiteDatabase sql1 = databaseHelper.getWritableDatabase();

                Cursor c1 = sql1.rawQuery(
                        "SELECT distinct * FROM "
                                + AnyMartDatabaseConstants.TABLE_MY_ORDER_HISTORY + " WHERE SOHeaderId ='"
                                + orderid + "' ORDER BY sono desc ", null);

                float amt = 0;
                if (c1.getCount() > 0) {
                    c1.moveToFirst();
                    do {
                        //   float amtofitem = c1.getFloat(c1.getColumnIndex("Rate"));
                        String o_date = c1.getString(c1.getColumnIndex("SODate"));
                        //   amt = amt + amtofitem;
                        bean.setSODate(o_date);
                        bean.setNetAmt(Float.parseFloat(c1.getString(c1.getColumnIndex("NetAmt"))));
                        bean.setDoAck(c1.getString(c1.getColumnIndex("DoAck")));
                        bean.setDODisptch(c1.getString(c1.getColumnIndex("DODisptch")));
                        bean.setDORcvd(c1.getString(c1.getColumnIndex("OrdRcvdDate")));
                        bean.setDOApprvd(c1.getString(c1.getColumnIndex("AppvDt")));
                        bean.setStatus(c1.getString(c1.getColumnIndex("status")));
                        bean.setStatusname(c1.getString(c1.getColumnIndex("statusname")));
                        bean.setDispatchNo(c1.getString(c1.getColumnIndex("DispatchNo")));
                        bean.setDispNetAmnt(Float.parseFloat(c1.getString(c1.getColumnIndex("DispNetAmnt"))));
                        bean.setSalesHeaderId(c1.getString(c1.getColumnIndex("SalesHeaderId")));
                        bean.setDOrej(c1.getString(c1.getColumnIndex("DOrej")));
                        bean.setShipstatus(c1.getString(c1.getColumnIndex("ShipStatus")));
                        bean.setAddress(c1.getString(c1.getColumnIndex("Address")));
                        bean.setMerchantname(c1.getString(c1.getColumnIndex("merchantname")));
                        bean.setOrgQty(c1.getString(c1.getColumnIndex("OrgQty")));
                        bean.setDeliveryTerms(c1.getString(c1.getColumnIndex("DeliveryTerms")));
                        bean.setMinordqty(c1.getString(c1.getColumnIndex("minordqty")));
                        bean.setMaxordqty(c1.getString(c1.getColumnIndex("maxordqty")));
                        bean.setDistance(c1.getString(c1.getColumnIndex("distance")));
                        bean.setUOMCode(c1.getString(c1.getColumnIndex("UOMCode")));
                        bean.setOutofstock(c1.getString(c1.getColumnIndex("outofstock")));
                        bean.setMrp(Float.parseFloat(c1.getString(c1.getColumnIndex("mrp"))));
                        bean.setSellingrate(c1.getString(c1.getColumnIndex("sellingrate")));
                        bean.setRange(c1.getString(c1.getColumnIndex("range")));
                        bean.setUOMDigit(c1.getString(c1.getColumnIndex("PurDigit")));

                    } while (c1.moveToNext());
                }
                historyBeanList.add(bean);
            } while (c.moveToNext());

            myOrderHistoryAdapter = new OpenOrderListAdapter(parent, historyBeanList);
            listview_recent_ordered_list.setAdapter(myOrderHistoryAdapter);
        } else {

        }
    }

    public void getLastOrdPlaceWith(){
        SQLiteDatabase sql = databaseHelper.getWritableDatabase();
        String qry = "Select merchantid,merchantname from "+AnyMartDatabaseConstants.TABLE_MY_ORDER_HISTORY+" WHERE status='10'" +
                " Order by DoAck DESC LIMIT 1";
        Cursor c = sql.rawQuery(qry,null);
        if(c.getCount() > 0){
            c.moveToFirst();
            do{
                lastOrd_MerchName = c.getString(c.getColumnIndex("merchantname"));
                lastOrd_MerchId = c.getString(c.getColumnIndex("merchantid"));
            }while (c.moveToNext());
        }

        if(shopByMerchant == true){

            if(AnyMartData.MerchantName.equals("") || AnyMartData.MerchantName.equals(null)){
                plcwith.setText(getResources().getString(R.string.bymerch));
                txt_lastmerch.setText(/*" "+getResources().getString(R.string.ordfrm)+" "+ */""+AnyMartData.MerchantName);
            }else {

                if(AnyMartData.SHOPBYMODE.equalsIgnoreCase("ShopBySpeciality")){
                    plcwith.setText(getResources().getString(R.string.cntnueshopp));
                    txt_lastmerch.setText(/*" "+getResources().getString(R.string.ordfrm)+" "+ */" - "+AnyMartData.MerchantName);
                }else {
                    plcwith.setText(getResources().getString(R.string.shopbymerch));
                    txt_lastmerch.setText(/*" "+getResources().getString(R.string.ordfrm)+" "+ */" - "+AnyMartData.MerchantName);
                    selprodcat.setText(getResources().getString(R.string.shopby)+" "+AnyMartData.MerchantName);
                }
            }

        }else {
            if(lastOrd_MerchName.equalsIgnoreCase("") || lastOrd_MerchName.equalsIgnoreCase(null)){
                plcwith.setText(getResources().getString(R.string.bymerch));
                txt_lastmerch.setText(/*" "+getResources().getString(R.string.ordfrm)+" "+ */""+lastOrd_MerchName);
            }else {
                if(AnyMartData.SHOPBYMODE.equalsIgnoreCase("ShopBySpeciality")){
                    plcwith.setText(getResources().getString(R.string.cntnueshopp));
                    txt_lastmerch.setText(/*" "+getResources().getString(R.string.ordfrm)+" "+ */" - "+lastOrd_MerchName);
                }else {
                    plcwith.setText(getResources().getString(R.string.shopbymerch));
                    txt_lastmerch.setText(/*" "+getResources().getString(R.string.ordfrm)+" "+ */" - "+lastOrd_MerchName);
                    selprodcat.setText(getResources().getString(R.string.shopby)+" "+lastOrd_MerchName);
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SegmentSelection && resultCode == SegmentSelection){
            //call to server API to get category against selected merchant only
           /* AnyMartData.selected_BSEGMENTID = data.getStringExtra("BSegmentId");
            AnyMartData.selected_BSEGMENTCODE = data.getStringExtra("BSegmentCode");
            AnyMartData.selected_MERCHID = data.getStringExtra("SelMerchId");
            AnyMartData.MerchantName = data.getStringExtra("SelMerchName");
            AnyMartData.MerchantID = AnyMartData.selected_MERCHID;*/

            /*SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("SelBSegId", AnyMartData.selected_BSEGMENTID);
            editor.putString("SelBSegCode", AnyMartData.selected_BSEGMENTCODE);
            editor.putString("SelMerchId", AnyMartData.selected_MERCHID);
            editor.putString("MerchantName", AnyMartData.MerchantName);
            editor.putString("MerchantID", AnyMartData.MerchantID);
            editor.commit();*/

            //txt_lastmerch.setText(/*" "+getResources().getString(R.string.ordfrm)+" "+*/" - "+ AnyMartData.MerchantName);

        }else  if (resultCode == REQ_CODE_VERSION_UPDATE && requestCode == REQ_CODE_VERSION_UPDATE) { //RESULT_OK / RESULT_CANCELED / RESULT_IN_APP_UPDATE_FAILED
          //  Log.d("Update flow failed! Result code: ", String.valueOf(resultCode));
            // If the update is cancelled or fails,
            // you can request to start the update again.
            if (resultCode != RESULT_OK) {
                log("Update flow failed! Result code: " + resultCode);
                // If the update is cancelled or fails,
                // you can request to start the update again.
                unregisterInstallStateUpdListener();
            }
        }{
          //  getDataFromDataBase();
        }
    }

    public String getAddressViaGPSCordinates(String Latitude, String Longitude,String addrGUID){
        String loclity = "",state = "",postalcode="";

        double lat = Double.parseDouble(Latitude);
        double longi =  Double.parseDouble(Longitude);

        AnyMartData.LATITUDE = Latitude;
        AnyMartData.LONGITUDE = Longitude;

        List<Address> addresses = null;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(lat, longi, 1);
            loclity = getAddreData(addresses).split(",")[0];
            state = getAddreData(addresses).split(",")[1];
            //postalcode = getAddreData(addresses).split(",")[2];
        } catch (Exception ioException) {
            Log.e("", "Error in getting address for the location");
        }

        ContentValues cv = new ContentValues();
        cv.put("Locality",loclity);
        cv.put("State",state);
        //cv.put("GeoLocation",loclity+","+postalcode);
        sql_db.update(AnyMartDatabaseConstants.TABLE_SHIPTO_DETAILS, cv, "GUID=?", new String[]{addrGUID});

        return loclity;
    }

    public String getAddreData(List<Address> addresses){
        String loclty = "",state = "", postlcode = "";
        if (addresses == null || addresses.size()  == 0) {
            String msg = "No address found for the location";
        } else {

            Address address = addresses.get(0);
            StringBuffer addressDetails = new StringBuffer();

            addressDetails.append(address.getFeatureName());
            addressDetails.append("\n");

            //edtlandmrk.setText(address.getFeatureName());       //landmark

            addressDetails.append(address.getThoroughfare());
            addressDetails.append("\n");

            addressDetails.append("Locality: ");
            addressDetails.append(address.getLocality());
            addressDetails.append("\n");

            addressDetails.append("County: ");
            addressDetails.append(address.getSubAdminArea());       //city
            addressDetails.append("\n");

            addressDetails.append("State: ");
            addressDetails.append(address.getAdminArea());      //state
            addressDetails.append("\n");

            addressDetails.append("Country: ");
            addressDetails.append(address.getCountryName());        //country
            addressDetails.append("\n");

            addressDetails.append("Postal Code: ");
            addressDetails.append(address.getPostalCode());     //pincode
            addressDetails.append("\n");

            addressDetails.append("Postal Code: ");
            addressDetails.append(address.getPremises());     //plotno, house no
            addressDetails.append("\n");

            addressDetails.append("Postal Code: ");
            addressDetails.append(address.getSubLocality());     //locality
            addressDetails.append("\n");

            loclty = address.getSubLocality();

            if(loclty == null || loclty =="null"){
                loclty = address.getSubAdminArea();
            }

            state = address.getAdminArea();
            postlcode=address.getPostalCode();
            AnyMartData.STATE =address.getAdminArea();
            //AnyMartData.PINCODE= address.getPostalCode();
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("State", AnyMartData.STATE);
            //editor.putString("Pincode", AnyMartData.PINCODE);
            editor.commit();
        }
        return loclty+","+state+","+postlcode;
    }

    public void checkForAppUpdate() {

        // Creates instance of the manager.
        appUpdateManager = AppUpdateManagerFactory.create(/*AppCustom.getAppContext()*/getApplicationContext());

        InstallStateUpdatedListener installStateUpdatedListener = installState -> {
            // Show module progress, log state, or install the update.
            if (installState.installStatus() == InstallStatus.DOWNLOADED) {
                // After the update is downloaded, show a notification
                // and request user confirmation to restart the app.
                //popupAlerter();
            }
        };

        // Before starting an update, register a listener for updates.
        appUpdateManager.registerListener(installStateUpdatedListener);

        // Returns an intent object that you use to check for an update.
        appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Checks that the platform will allow the specified type of update.// Start an update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    /*&& appUpdateInfo.clientVersionStalenessDays() != null
                    && appUpdateInfo.clientVersionStalenessDays() >= 5*/
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                // Start an update.
                startAppUpdateImmediate(appUpdateInfo);
            }else if(appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE){
                startAppUpdateImmediate(appUpdateInfo);
            }else {
                startAppUpdateImmediate(appUpdateInfo);
            }
        });

    // When status updates are no longer needed, unregister the listener.
      //  appUpdateManager.unregisterListener(listener);
    }

    public void startAppUpdateImmediate(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.IMMEDIATE,
                    // The current activity making the update request.
                    this,
                    // Include a request code to later monitor this update request.
                   REQ_CODE_VERSION_UPDATE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startAppUpdateFlexible(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.FLEXIBLE,
                    // The current activity making the update request.
                    this,
                    // Include a request code to later monitor this update request.
                    REQ_CODE_VERSION_UPDATE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
            unregisterInstallStateUpdListener();
        }
    }

    /* Displays the snackbar notification and call to action. */
    private void popupSnackbarForCompleteUpdate() {
        /*Snackbar snackbar = Snackbar.make(coordinatorLayout,
                        "An update has just been downloaded.",
                        Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("RESTART", view -> appUpdateManager.completeUpdate());
        snackbar.setActionTextColor(
                getResources().getColor(R.color.green));
        snackbar.show();*/
    }

    public InstallStateUpdatedListener installStateUpdatedListener = installState -> {
        // Show module progress, log state, or install the update.
        if (installState.installStatus() == InstallStatus.DOWNLOADED) {
            // After the update is downloaded, show a notification
            // and request user confirmation to restart the app.
            //popupAlerter();
        }
    };

    public void unregisterInstallStateUpdListener() {
        if (appUpdateManager != null && installStateUpdatedListener != null)
            appUpdateManager.unregisterListener(installStateUpdatedListener);
    }

    private void checkNewAppVersionState() {
        appUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
                    @Override
                    public void onSuccess(AppUpdateInfo appUpdateInfo) {
                        //FLEXIBLE:
                        // If the update is downloaded but not installed,
                        // notify the user to complete the update.
                        if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                           // popupAlerter();
                           // Log.d(TAG, "checkNewAppVersionState(): resuming flexible update. Code: " + appUpdateInfo.updateAvailability());
                        }
                    }
                });
    }

    public void setGridViewHeightBasedOnChildren(GridView gridView, int columns) {
        try{
            ListAdapter listAdapter = gridView.getAdapter();
            if (listAdapter == null) {
                // pre-condition
                return;
            }

            int totalHeight = 0;
            int items = listAdapter.getCount();
            int rows = 0;

            View listItem = listAdapter.getView(0, null, gridView);
            listItem.measure(0, 0);
            totalHeight = listItem.getMeasuredHeight();

            float x = 1;
            if( items > columns ){
                x = items/columns;
                rows = (int) (x+1);
                totalHeight *= rows;
            }

            ViewGroup.LayoutParams params = gridView.getLayoutParams();
            params.height = totalHeight;
            gridView.setLayoutParams(params);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /*private void popupAlerter() {
        Alerter.create(this)
                .setTitle(R.string.app_update)
                .setDuration(15000) //15 secs
                .setBackgroundColorRes(R.color.colorPrimary)
                .setIcon(R.drawable.ic_stat_onesignal_default)
                .setText("An update has just been downloaded. Please restart you application to install the update")
                .addButton("Restart", R.style.AlertButton, v -> appUpdateManager.completeUpdate()).show();
    }*/

    @Override
    protected void onDestroy() {
        //unregisterInstallStateUpdListener();
        super.onDestroy();
    }

   /* public void inAppUpdate(){
        String cv = String.valueOf(BuildConfig.VERSION_CODE);

        // Initialize the Update Manager with the Activity and the Update Mode
        mUpdateManager = UpdateManager.Builder(this);

        // Callback from UpdateInfoListener
        // You can get the available version code of the apk in Google Play
        // Number of days passed since the user was notified of an update through the Google Play
        mUpdateManager.addUpdateInfoListener(new UpdateInfoListener() {
            @Override
            public void onReceiveVersionCode(final int code) {
                txtAvailableVersion.setText(String.valueOf(code));
            }

            @Override
            public void onReceiveStalenessDays(final int days) {
                txtStalenessDays.setText(String.valueOf(days));
            }
        });

        // Callback from Flexible Update Progress
        // This is only available for Flexible mode
        // Find more from https://developer.android.com/guide/playcore/in-app-updates#monitor_flexible
        mUpdateManager.addFlexibleUpdateDownloadListener(new FlexibleUpdateDownloadListener() {
            @Override
            public void onDownloadProgress(final long bytesDownloaded, final long totalBytes) {
                txtFlexibleUpdateProgress.setText("Downloading: " + bytesDownloaded + " / " + totalBytes);
            }
        });
    }*/

    public ArrayList<AllCatSubcatItems> filter_serch(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        serchProd.clear();
        if (charText.length() == 0) {
            serchProd.addAll(serchProd1);
        } else {
            for (AllCatSubcatItems wp : serchProd1) {
                if (wp.getItemName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    serchProd.add(wp);
                    tempList.clear();
                    tempList.addAll(serchProd1);
                }
            }

            if(serchProd.size() == 0){
                //Toast.makeText(parent,"Call server API",Toast.LENGTH_SHORT).show();
                String selProduct="",selProdId="",selItmImgPath="";
                for(int i=0; i<serchProd.size();i++){
                    if(edtsearch_product.getText().toString().equalsIgnoreCase(serchProd.get(i).getItemName())){
                        selProduct = serchProd.get(i).getItemName();
                        selProdId = serchProd.get(i).getItemMasterId();
                        selItmImgPath = serchProd.get(i).getItemImgPath();
                    }
                }

                //send to ItemMultimerch screen
                Intent intent = new Intent(MainActivity.this, Multimerchant_ProductListActivity.class);
                intent.putExtra("ItemMasterID","");
                intent.putExtra("ItemDesc",charText);
                intent.putExtra("ItemImgPath","");
                intent.putExtra("KeyCall","Search_ItemId");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }

        return serchProd1;
    }

    /*****************************************Get subcategory data*************************************************************/
    public class GetMerchFamilyMaster_subcategory extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String responseString = "";
        String response_list = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

           /* try{
                progress = ProgressHUD.show(parent,
                        ""+getResources().getString(R.string.loading_subcat), false, true, null);
            }catch (Exception e){
                e.printStackTrace();
            }*/
        }

        @Override
        protected String doInBackground(String... params) {

            //old familymasterdata all families
            String url_getCategory_List = "";
            if(AnyMartData.SHOPBYMODE.equalsIgnoreCase("ShopBySpeciality")){
                url_getCategory_List = AnyMartData.MAIN_URL + AnyMartData.METHOD_GET_FAMILY_MASTER_MERCHANT +
                        "?handler=" + AnyMartData.HANDLE +
                        "&sessionid=" + AnyMartData.SESSION_ID+
                        "&MerchantId=&type=SubCategory&BusSegmentId="+AnyMartData.selected_BSEGMENTID+"&CategoryId="+selectedCatid;
            }else {
                url_getCategory_List = AnyMartData.MAIN_URL + AnyMartData.METHOD_GET_FAMILY_MASTER_MERCHANT +
                        "?handler=" + AnyMartData.HANDLE +
                        "&sessionid=" + AnyMartData.SESSION_ID+
                        "&MerchantId=" + AnyMartData.selected_MERCHID+
                        "&type=SubCategory&BusSegmentId=" + AnyMartData.selected_BSEGMENTID+"&CategoryId="+selectedCatid;
            }

            String getcatSubItems = url_getCategory_List;
            URLConnection urlConnection = null;
            BufferedReader bufferedReader = null;
            try {

                res = Utility.OpenconnectionOrferbilling(url_getCategory_List, parent);
                int a = res.getBytes().length;
                res = res.replaceAll("\\\\", "");
                // res = res.replaceAll("\"", "");
                // res = res.replaceAll(" ", "");
                responseString = res.toString().replaceAll("^\"|\"$", "");
                Log.e("Response", responseString);

                /*responseString = stringBuff_getItems.toString().replaceAll("^\"|\"$", "");*/
                response_list = responseString.replaceAll("\\\\", "");
                System.out.println("resp =" + response_list);

            } catch (Exception e) {
                response_list = "error";
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try{
                progress.dismiss();
            }catch (Exception e){
                e.printStackTrace();
            }

            try{
                if (response_list.equalsIgnoreCase("Session Expired")) {
                    if (NetworkUtils.isNetworkAvailable(parent)) {
                        new StartSession(parent, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new GetMerchFamilyMaster_subcategory().execute();
                            }

                            @Override
                            public void callfailMethod(String s) {

                            }
                        });
                    }
                } else if (response_list.equalsIgnoreCase("error")) {
                    Toast.makeText(parent, ""+parent.getResources().getString(R.string.servererror), Toast.LENGTH_LONG).show();
                   // txtnoordnote.setVisibility(View.VISIBLE);
                    txtlaunchsoon.setText(getResources().getString(R.string.thanks_for_interes)+" "+selectedCatName+" "+
                            getResources().getString(R.string.soon));
                } else if (response_list.contains("sesEndTime")) {
                 //   txtnoordnote.setVisibility(View.VISIBLE);
                    txtlaunchsoon.setText(getResources().getString(R.string.thanks_for_interes)+" "+selectedCatName+" "+
                            getResources().getString(R.string.soon));
                }else {
                 //   txtnoordnote.setVisibility(View.GONE);
                    json = response_list;
                    parseJson_MerchFamilyMaster_subcategory(json);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    protected void parseJson_MerchFamilyMaster_subcategory(String json) {
        Utilities.clearTable(parent, AnyMartDatabaseConstants.TABLE_FAMILY_MASTERDATA);

        arrayList_subcat.clear();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {

                String SubCatImgPath = "";
                SubCatImgPath =  jsonArray.getJSONObject(i).getString("SubCatImgPath");
                AnyMartData.SubCatImgPath = SubCatImgPath;

               /* SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("CatImgPath", AnyMartData.CatImgPath);
                editor.putString("SubCatImgPath", AnyMartData.SubCatImgPath);
                editor.commit();*/

                if(SubCatImgPath.equalsIgnoreCase("") || SubCatImgPath.equalsIgnoreCase(null)){
                    SubCatImgPath = "";
                }else {
                    SubCatImgPath = AnyMartData.CompanyURL+"/images/"+SubCatImgPath;
                }

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("CatImgPath", AnyMartData.CatImgPath);
                editor.putString("SubCatImgPath", SubCatImgPath);
                editor.commit();

                databaseHelper.addFamilyMaster(jsonArray.getJSONObject(i).getString("CategoryId"),
                        jsonArray.getJSONObject(i).getString("CategoryName"),
                        jsonArray.getJSONObject(i).getString("SubCategoryId"),
                        jsonArray.getJSONObject(i).getString("SubCategoryName"),
                        "",AnyMartData.CatImgPath, SubCatImgPath,
                        String.valueOf(SubCatCount), jsonArray.getJSONObject(i).getString("ItemCount"));

                if(AnyMartData.AppCode.equalsIgnoreCase("SM")||
                        shopByMerchant == true && AnyMartData.SHOPBYMODE.equalsIgnoreCase("ShopByMerchant")){

                    Intent intent = new Intent(parent, ItemListActivity.class);
                    intent.putExtra("SubCategoryId", jsonArray.getJSONObject(i).getString("SubCategoryId"));
                    intent.putExtra("SubCategoryName",  jsonArray.getJSONObject(i).getString("SubCategoryName"));
                    intent.putExtra("CategoryName",  jsonArray.getJSONObject(i).getString("CategoryName"));
                    intent.putExtra("CustomerID",CustomerID);
                    intent.putExtra("CallType","SubCategorySearch");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.enter_right_to_left,R.anim.exit_left_to_right);
                }else {
                    Intent intent = new Intent(parent, ItemListActivity_Multimerchant.class);
                    intent.putExtra("SubCategoryId", jsonArray.getJSONObject(i).getString("SubCategoryId"));
                    intent.putExtra("SubCategoryName", jsonArray.getJSONObject(i).getString("SubCategoryName"));
                    intent.putExtra("CategoryName", jsonArray.getJSONObject(i).getString("CategoryName"));
                    intent.putExtra("CustomerID",CustomerID);
                    //intent.putExtra("PurDigit",PurDigit);
                    intent.putExtra("CallType","SubCategorySearch");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.enter_right_to_left,R.anim.exit_left_to_right);
                }
            }

           /* try{
                progress.dismiss();
            }catch (Exception e){
                e.printStackTrace();
            }*/

           // getDataFromDataBase();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getItemFromServer(String charText){
        //Toast.makeText(parent,"Call server API",Toast.LENGTH_SHORT).show();

        //send to ItemMultimerch screen
        Intent intent = new Intent(MainActivity.this, Multimerchant_ProductListActivity.class);
        intent.putExtra("ItemMasterID","");
        intent.putExtra("ItemDesc",charText);
        intent.putExtra("ItemImgPath","");
        intent.putExtra("KeyCall","Search_Brand_Item_Name");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void updateList(List<AllCatSubcatItems> arraylist) {
        tempList.clear();
        tempList.addAll(arraylist);
    }

    public void getBannersFromServer(){
        if (NetworkUtils.isNetworkAvailable(MainActivity.this)) {
            new GetBannerData().execute();
        }
    }

    private class GetBannerData extends AsyncTask<Void, Void, JSONArray> {
        String responseString = "", res = "";
        String respSupp = "";

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected JSONArray doInBackground(Void... voids) {

            String url_States = AnyMartData.MAIN_URL + AnyMartData.api_GetAnydukaanBanner;

            URLConnection urlConnection = null;
            BufferedReader bufferedReader = null;

            try {

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                res = Utility.OpenconnectionOrferbilling(url_States, parent);
                int a = res.getBytes().length;
                res = res.replaceAll("\\\\", "");
                responseString = res.toString().replaceAll("^\"|\"$", "");
                respSupp = responseString.toString().replaceAll("\\\\", "");
                String rs = respSupp;
                Log.e("Response", respSupp);

                jrresult = new JSONArray(respSupp);


            } catch (Exception e) {
                respSupp = "error";
                e.printStackTrace();
            }
            return jrresult;
        }

        @Override
        protected void onPostExecute(JSONArray result){
            super.onPostExecute(result);

            try{
                jrresult_supp = new JSONArray(respSupp);

                bannerList.clear();

                for(int i=0; i<jrresult_supp.length();++i){
                    JSONObject jsonObject = jrresult_supp.getJSONObject(i);
                    String BannerType = jsonObject.getString("BannerType");
                    String BannerFileName = jsonObject.getString("BannerFileName");
                    String ValidFrom = jsonObject.getString("ValidFrom");
                    String ValidTo = jsonObject.getString("ValidTo");
                    String BannerClickAction = jsonObject.getString("BannerClickAction");
                    String BannerClickActionURL = jsonObject.getString("BannerClickActionURL");

                    Banner bannr = new Banner();
                    bannr.setBannerType(BannerType);
                    bannr.setBannerFileName(BannerFileName);
                    bannr.setValidFrom(ValidFrom);
                    bannr.setValidTo(ValidTo);
                    bannr.setBannerClickAction(BannerClickAction);
                    bannr.setBannerClickActionURL(BannerClickActionURL);

                    bannerList.add(bannr);

                    String imgbnnr = AnyMartData.CompanyURL+"/images/"+BannerFileName;
                    bannerDispList.add(imgbnnr);

                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("BannerList", imgbnnr);
                    editor.commit();
                }

            }catch (Exception e){
                e.printStackTrace();
            }

            DisplayBanner(bannerDispList);
        }
    }

    public void DisplayBanner(ArrayList<String> bannerDispList){

        viewPager = (ViewPager) findViewById(R.id.viewPager);
       // ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this,imgthumbIds);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this,bannerDispList);
        viewPager.setAdapter(viewPagerAdapter);

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES-1) {
                    currentPage = 0;
                }

                viewPager.setCurrentItem(currentPage++, true);
                if (currentPage >= bannerDispList.size()) {
                    currentPage = 0;
                }else {

                    //play video
                   /* try{
                        pathVideo = "android.resource://" + getPackageName() + "/" + R.raw.baneer_vid1;
                        videoView.setVideoURI(Uri.parse(pathVideo));
                        videoView.start();
                    }catch (Exception e){
                        e.printStackTrace();
                    }*/
                }
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);
    }

    public void onCoachMark(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.coach_mark);
        dialog.setCanceledOnTouchOutside(true);
        //for dismissing anywhere you touch
        View masterView = dialog.findViewById(R.id.coach_mark_master_view);

        masterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}




