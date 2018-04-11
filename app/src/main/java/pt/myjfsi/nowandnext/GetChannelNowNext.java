package pt.myjfsi.nowandnext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetChannelNowNext {

    private Webservice myWebservice = new Webservice();

    /************************************************* ********************************************
    Function to load the current program and the next one.
    It's always called, even when the channel page already existed.
    At the same time it loads the image of the current program (myWebservice.WebserviceImage).
    ************************************************** *******************************************/
    public void getNowNext(){
        if (!Globals.CList.isEmpty()) {
            for (int i = 0; i < Globals.CList.size(); i++) {

                Globals.ChannelList cChannelList = Globals.CList.get(i);
                if (cChannelList.getChannelPage() == Globals.iChannelPage) {

                    String sUrl =   "http://ott.online.meo.pt/Program/v7/Programs/NowAndNextLiveChannelPrograms?UserAgent=AND&$filter=CallLetter%20eq%20%27" +
                                    cChannelList.getCallLetter() +
                                    "%27&$orderby=StartDate%20asc";

                    String jsonStr = myWebservice.WebserviceGet(sUrl, Globals.iTimeout);

                    if (jsonStr != null) {
                        try {
                            JSONObject jsonObj = new JSONObject(jsonStr.substring(jsonStr.indexOf("{"), jsonStr.lastIndexOf("}") + 1));

                            if (jsonObj.has("value")) {

                                // Getting JSON Array node
                                JSONArray ArrLines = jsonObj.getJSONArray("value");

                                // looping through All Contacts
                                for (int d = 0; d < 2; d++) {
                                    JSONObject c = ArrLines.getJSONObject(d);
                                    if (c.has("Title")) {
                                        if (d==0){
                                            cChannelList.SetNowTitle(c.getString("Title"));

                                            // Get Now Image
                                            String image_url = "http://proxycache.app.iptv.telecom.pt:8080/eemstb/ImageHandler.ashx?evTitle=" +
                                                    c.getString("Title").replace(" ", "+") +
                                                    "&chCallLetter=" +
                                                    cChannelList.getCallLetter() +
                                                    "&profile=16_9&width=320";

                                            cChannelList.SetbmpNowImage(myWebservice.WebserviceImage(image_url, Globals.iImageTimeout));
                                        }
                                        else{
                                            cChannelList.SetNextTitle(c.getString("Title"));
                                        }
                                    }
                                }

                                Globals.CList.set( i, cChannelList);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        }
    }
}
