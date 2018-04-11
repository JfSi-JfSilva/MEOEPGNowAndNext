package pt.myjfsi.nowandnext;

import android.graphics.Bitmap;

import java.util.ArrayList;


/************************************************* ********************************************
Class of global variables to the program such as:
Page;
List of channels;
Timeout for web information;
Timeout to get web image;
etc.
************************************************** *******************************************/
public final class Globals {

    public static class ChannelList
    {
        int    iChannelPage;
        String sCallLetter;
        String sChannelTitle;
        String sNowTitle;
        String sNextTitle;
        String sNextChannelPageUri;
        Bitmap bmpNowImage;

        ChannelList(int iChannelPage,
                    String sCallLetter,
                    String sChannelTitle,
                    String sNextChannelPageUri)
        {
            this.iChannelPage = iChannelPage;
            this.sCallLetter = sCallLetter;
            this.sChannelTitle = sChannelTitle;
            this.sNextChannelPageUri = sNextChannelPageUri;
        }

        public void SetNowTitle( String sNowTitle)
        {
            this.sNowTitle = sNowTitle;
        }

        public void SetNextTitle( String sNextTitle)
        {
            this.sNextTitle = sNextTitle;
        }

        public void SetbmpNowImage( Bitmap bmpNowImage)
        {
            this.bmpNowImage = bmpNowImage;
        }

        public int getChannelPage()
        {
            return this.iChannelPage;
        }

        public String getCallLetter()
        {
            return this.sCallLetter;
        }

        public String getChannelTitle()
        {
            return this.sChannelTitle;
        }

        public String getNowTitle()
        {
            return this.sNowTitle;
        }

        public String getNextTitle()
        {
            return this.sNextTitle;
        }

        public String getNextChannelPageUri()
        {
            return this.sNextChannelPageUri;
        }

        public Bitmap getbmpNowImage() {
            return this.bmpNowImage;
        }
    }

    public static ArrayList<ChannelList> CList = new ArrayList<>();

    public static int iChannelPage = 0;
    public static int iTimeout = 60;
    public static int iImageTimeout = 120;
}
