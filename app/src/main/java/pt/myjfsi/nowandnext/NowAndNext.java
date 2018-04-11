package pt.myjfsi.nowandnext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/************************************************* ********************************************
Program: NowAndNext
Obeter Meo Mobile's channel list goes to the respective web services.
A page is defined that respects the paging of service channels.
When entering, it loads the information from the first page of channels.
It has the following buttons:
Up:
The page is decremented to the limit of page 0 and get the channels and
respective information .
Note that the channel list is not brought back, just its content.
Refresh:
Loads its information to the channels on the page.
Down.
The page is incremented and gets the respective channels and information.
If the page has never been loaded behind the list of channels and their information, if
otherwise loads its information into the channels of the page.
Developed by: Jorge F. Silva
Date: 2018-11-04
************************************************** *******************************************/
public class NowAndNext extends AppCompatActivity {

    GetChannelList myGetChannelList = new GetChannelList();

    ImageButton imgBtnUp;
    ImageButton imgBtnRefresh;
    ImageButton imgBtnDown;
    LinearLayout linlProgressbar;

    AsyncTaskGetInfo AsyncGetInfo = new AsyncTaskGetInfo();

    private Handler mHandler = new Handler();

    private final Runnable mRunnable = new Runnable() {
        public void run() {
            if (!(AsyncGetInfo.getStatus() == AsyncTask.Status.RUNNING)) {
                AsyncGetInfo = new AsyncTaskGetInfo();
                AsyncGetInfo.execute(NowAndNext.this.getApplicationContext());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Globals.iChannelPage = 0;

        setContentView(R.layout.activity_now_and_next);

        imgBtnUp = findViewById(R.id.imgBtnUp);
        imgBtnUp.setOnClickListener(
                new ImageButton.OnClickListener () {
                    @Override // to override the method
                    public void onClick(View view) {
                        if (!(AsyncGetInfo.getStatus() == AsyncTask.Status.RUNNING)) {
                            Globals.iChannelPage--;

                            if (Globals.iChannelPage < 0) {
                                Globals.iChannelPage = 0;
                            }

                            mHandler.removeCallbacks(mRunnable);
                            mHandler.post(mRunnable);
                        }
                    }
                }
        );

        imgBtnRefresh = findViewById(R.id.imgBtnRefresh);
        imgBtnRefresh.setOnClickListener(
                new ImageButton.OnClickListener () {
                    @Override // to override the method
                    public void onClick(View view) {
                        if (!(AsyncGetInfo.getStatus() == AsyncTask.Status.RUNNING)) {
                            mHandler.removeCallbacks(mRunnable);
                            mHandler.post(mRunnable);
                        }
                    }
                }
        );

        imgBtnDown = findViewById(R.id.imgBtnDown);
        imgBtnDown.setOnClickListener(
                new ImageButton.OnClickListener () {
                    @Override // to override the method
                    public void onClick(View view) {
                        if (!(AsyncGetInfo.getStatus() == AsyncTask.Status.RUNNING)) {
                            Globals.iChannelPage++;

                            mHandler.removeCallbacks(mRunnable);
                            mHandler.post(mRunnable);
                        }
                    }
                }
        );


        linlProgressbar = findViewById(R.id.progressbar_view);
    }

    @Override
    public void onResume() {
        super.onResume();

        mHandler.removeCallbacks(mRunnable);
        mHandler.post(mRunnable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mHandler.removeCallbacks(mRunnable);
    }

    /************************************************* ********************************************
    Asynchronous task for necessary information
    ************************************************** *******************************************/
    class AsyncTaskGetInfo extends AsyncTask<Context, Void, Void> {
        Context AsyncCtx;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            linlProgressbar.setVisibility(View.VISIBLE);
        }

        protected void onPostExecute(Void in) {
            super.onPostExecute(in);

            final TableLayout tbl1 = findViewById(R.id.tableLayout1);
            tbl1.removeAllViews();

            if (!Globals.CList.isEmpty()) {
                for (int i = 0; i < Globals.CList.size(); i++) {

                    Globals.ChannelList cChannelList = Globals.CList.get(i);
                    if (cChannelList.getChannelPage() == Globals.iChannelPage) {

                        @SuppressLint("InflateParams") final TableRow row = (TableRow) LayoutInflater.from(AsyncCtx).inflate(R.layout.trow_nowandnext, null);

                        ImageView imgVFolder = row.findViewById(R.id.imgVFolder);
                        imgVFolder.setImageBitmap(cChannelList.getbmpNowImage());

                        TextView tvChannel = row.findViewById(R.id.tvChannel);
                        tvChannel.setText(cChannelList.getChannelTitle());

                        TextView tvShowNow = row.findViewById(R.id.tvShowNow);
                        tvShowNow.setText(cChannelList.getNowTitle());

                        TextView tvShowNext = row.findViewById(R.id.tvShowNext);
                        tvShowNext.setText(cChannelList.getNextTitle());

                        tbl1.addView(row);
                    }
                }

                tbl1.requestLayout();
                tbl1.setVisibility(View.VISIBLE);

                linlProgressbar.setVisibility(View.GONE);

                // Every 5 minutes refresh this page channel list
                mHandler.postDelayed(mRunnable, 5 * 60 * 1000);
            }
        }

        protected Void doInBackground(Context... params) {
            AsyncCtx = params[0].getApplicationContext();

            myGetChannelList.GetChannel();

            return null;
        }
    }
}
