package pt.myjfsi.nowandnext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetChannelList {

    private Webservice myWebservice = new Webservice();
    private GetChannelNowNext myGetChannelNowNext = new GetChannelNowNext();

    /************************************************* ********************************************
    Function for obtaining channel information.
    Prepared to read the channels per page, i.e., it will load the channels according to
    with the paging of the mother's canals.
    Pages are incremented by the down button and
    decremented by the up button.
    It has no input parameters because the page is a global variable
    It has no output parameters, however it loads the global list variable
    channels (GetPageChannelWebInfo)
    ************************************************** *******************************************/
    public void GetChannel(){
        String sNextChannelPageUri = "";
        Boolean bChannelPageFound = false;

        //
        // Check if channel page as already been loaded with channels
        //
        if (!Globals.CList.isEmpty()) {
            for (int i = 0; i < Globals.CList.size(); i++) {
                Globals.ChannelList cChannelList = Globals.CList.get(i);
                if (cChannelList.getChannelPage() == Globals.iChannelPage){
                    bChannelPageFound = true;
                    break;
                }

                sNextChannelPageUri = cChannelList.getNextChannelPageUri();
            }
        }

        //
        // IF channel Page as already been loaded only need to load Now and Next to supply up to date info
        //
        if (bChannelPageFound)
        {
            // Only need to reload now and after foreach channel in list of channels that already exist
            myGetChannelNowNext.getNowNext();
        }
        else
        {
            if (Globals.iChannelPage == 0 && sNextChannelPageUri.isEmpty()){
                // First channel page to load
                sNextChannelPageUri = "http://ott.online.meo.pt/catalog/v7/Channels?UserAgent=AND&$filter=substringof(%27MEO_Mobile%27,AvailableOnChannels)%20and%20IsAdult%20eq%20false&$orderby=ChannelPosition%20asc&$inlinecount=allpages";
                GetPageChannelWebInfo(sNextChannelPageUri);
            }
            else {
                if (sNextChannelPageUri.isEmpty()) {
                    Globals.iChannelPage--;
                    if (Globals.iChannelPage < 0){
                        Globals.iChannelPage = 0;
                    }

                    // Check again for previous channel page
                    // may have passed the last possible channel page
                    GetChannel();
                }
                else
                {
                    // Get the channel info
                    GetPageChannelWebInfo(sNextChannelPageUri);
                }
            }
        }

    }

    /************************************************** *********************************************
    Function to load the global channel list variable according to the selected page (Globals.CList).
    It is only called when the page has never been loaded
    *************************************************** ********************************************/
    private void GetPageChannelWebInfo(String sUrl) {
        // Get the channel info
        String jsonStr = myWebservice.WebserviceGet(sUrl, Globals.iTimeout);

        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr.substring(jsonStr.indexOf("{"), jsonStr.lastIndexOf("}") + 1));

                if (jsonObj.has("value")) {

                    // Getting JSON Array node
                    JSONArray ArrLines = jsonObj.getJSONArray("value");

                    // looping through All Contacts
                    for (int i = 0; i < ArrLines.length(); i++) {
                        JSONObject c = ArrLines.getJSONObject(i);
                        if (c.has("Title") && c.has("CallLetter")) {
                            String sNextChannelPageUri = "";
                            if (jsonObj.has("odata.nextLink")){
                                sNextChannelPageUri = jsonObj.getString("odata.nextLink").replace("%28", "(");
                                sNextChannelPageUri = sNextChannelPageUri.replace("%29", ")");
                            }
                            Globals.ChannelList cChannelList = new Globals.ChannelList(Globals.iChannelPage, c.getString("CallLetter"), c.getString("Title"), sNextChannelPageUri);
                            Globals.CList.add(cChannelList);
                        }
                    }

                    // Only need to reload now and after foreach channel in page
                    myGetChannelNowNext.getNowNext();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
