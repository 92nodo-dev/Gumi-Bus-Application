package e.user.gumibusremake;

import android.content.Intent;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import static android.support.v4.content.ContextCompat.startActivity;

public class TaskManager extends AppCompatActivity {

    public void getRouteAcctoThrghSttnList(String busId) throws IOException //노선별 경유정류소 목록
    {
        JsoupAsyncTask myTask = new JsoupAsyncTask();
        myTask.doInBackground("getRouteAcctoThrghSttnList", busId);
    }

    public void getSttnAcctoArvlPrearngeInfoList(String stopId) throws IOException //정류소별도착예정정보목록조회
    {
        JsoupAsyncTask myTask = new JsoupAsyncTask();
        myTask.doInBackground("getSttnAcctoArvlPrearngeInfoList", stopId);
    }

    public void getAllBusList() throws IOException //전체 버스 노선 목록
    {
        try {
            new JsoupAsyncTask().execute("getAllBusList", "None").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void getAllStopList() throws IOException // 전체 정류소 목록
    {
        try {
            new JsoupAsyncTask().execute("getAllStopList", "None").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class JsoupAsyncTask extends AsyncTask<String, String, Void> {

        ArrayList<BusStop> stopList = new ArrayList<BusStop>();
        ArrayList<ArriveData> adList = new ArrayList<ArriveData>();
        ArrayList<Bus> busList = new ArrayList<Bus>();
        String func;
        @Override

        protected Void doInBackground(String... params) {
            func = params[0];
            switch(params[0])
            {
                case "getRouteAcctoThrghSttnList":
                    try {
                        Document doc = Jsoup.connect("http://openapi.tago.go.kr/openapi/service/BusRouteInfoInqireService/getRouteAcctoThrghSttnList?"
                                + "serviceKey=yourKEY"
                                + "numOfRows=50&"
                                + "pageSize=50"
                                + "&pageNo=1"
                                + "&startPage=1"
                                + "&cityCode=37050"
                                + "&routeId=" + params[1]).get();
                        String html = doc.html();

                        String[] token = html.split("\n");
                        for(int i = 0; i < token.length; i++)
                        {
                            if(token[i].contains("<nodeid>") == true)
                            {
                                BusStop busStop = new BusStop();
                                busStop.stopId = token[i + 1].substring(5);
                                stopList.add(busStop);
                            }
                        }
                        int idx = 0;
                        for(int i = 0; i < token.length; i++)
                        {
                            if(token[i].contains("<nodenm>") == true)
                            {
                                stopList.get(idx++).stopName = token[i + 1].substring(5);
                            }
                        }

                        for(int i = 0; i < stopList.size(); i++)
                        {
                            System.out.println(stopList.get(i).stopId + "\t" + stopList.get(i).stopName);
                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                case "getSttnAcctoArvlPrearngeInfoList" :
                    try {
                        Document doc = Jsoup.connect("http://openapi.tago.go.kr/openapi/service/ArvlInfoInqireService/getSttnAcctoArvlPrearngeInfoList?"
                                + "serviceKey=yourKEY"
                                + "cityCode=37050&"
                                + "nodeId=" + params[1]).get();
                        String html = doc.html();

                        String[] token = html.split("\n");
                        System.out.println(html);

                        //남은 정류장 수
                        for(int i = 0; i < token.length; i++)
                        {
                            if (token[i].contains("<arrprevstationcnt>"))
                            {
                                ArriveData ad = new ArriveData();
                                ad.arrprevstationcnt = Integer.parseInt(token[i + 1].substring(5));
                                adList.add(ad);
                            }
                        }

                        //남은 시간
                        int idx = 0;
                        for(int i = 0; i < token.length; i++)
                        {
                            if (token[i].contains("<arrtime>"))
                            {
                                adList.get(idx++).arriveTime = Integer.parseInt(token[i + 1].substring(5));
                            }
                        }
                        idx = 0;

                        //버스 id
                        for(int i = 0; i < token.length; i++)
                        {
                            if (token[i].contains("<routeid>"))
                            {
                                adList.get(idx++).busId = token[i + 1].substring(5);
                            }
                        }
                        idx = 0;

                        //버스 번호
                        for(int i = 0; i < token.length; i++)
                        {
                            if (token[i].contains("<routeno>"))
                            {
                                adList.get(idx++).busNo = token[i + 1].substring(5);
                            }
                        }
                        idx = 0;

                        System.out.println("남은정류장수 남은시간\t버스id\t\t버스번호");
                        for(int i = 0; i < adList.size(); i++)
                        {
                            System.out.println(adList.get(i).arrprevstationcnt + "\t" + adList.get(i).arriveTime + "\t" + adList.get(i).busId + "\t" + adList.get(i).busNo);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                case "getAllBusList" :
                    try {
                        String url = "http://bis.gumi.go.kr/city_bus/city_route_service.do";
                        Document doc = Jsoup.connect(url).get();
                        String html = doc.html();

                        String sp = "</option>";
                        String[] token = html.split(sp);
                        //System.out.println(html);

                        for (int i = 0; i < token.length; i++) {
                            if (token[i].contains("<option value=")) {
                                Bus bus = new Bus();
                                bus.busId = token[i].substring(16, token[i].length()).split("\">")[0];
                                bus.busNo = token[i].substring(16, token[i].length()).split("\">")[1];
                                busList.add(bus);
                            }
                        }
                        busList.remove(0);

                        for (int i = 0; i < busList.size(); i++) {
                            Bus bus = busList.get(i);
                            bus.busId = "GMB" + bus.busId;
                            busList.set(i, bus);
                        }

                        BusNameFragment myBusName = new BusNameFragment();
                        Bundle myBundle = new Bundle();
                        myBundle.putSerializable("busList", busList);
                        myBusName.setArguments(myBundle);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                case "getAllStopList" :
                    try {
                        String url = "http://bis.gumi.go.kr/city_bus/city_station_service.do";
                        Document doc = Jsoup.connect(url).get();
                        String html = doc.html();

                        String[] numtoken = html.split("</td>");
                        ArrayList<String> numlist = new ArrayList<String>();

                        for (int i = 0; i < numtoken.length; i++) {
                            if (numtoken[i].contains("<td>"))
                                numlist.add(numtoken[i].split("<td>")[1]);
                        }
                        numlist.remove(0);
                        numlist.remove(0);
                        numlist.remove(numlist.size() - 1);
                        numlist.remove(numlist.size() - 1);

                        for (int i = 0; i < numlist.size(); i++) {
                            numlist.set(i, "GMB" + Integer.toString(Integer.parseInt(numlist.get(i)) - 10000));
                        }

                        String[] stoptoken = html.split("</td>");
                        ArrayList<String> stoplist = new ArrayList<String>();

                        for (int i = 0; i < stoptoken.length - 8; i++) {
                            if (stoptoken[i].contains("<td class=\"stop_name\">"))
                                stoplist.add(stoptoken[i].split("<td class=\\\"stop_name\\\">")[1]);//System.out.println("a" + stoptoken[i] + "a");//
                        }
                        System.out.println(stoplist.get(2));
                        for (int i = 0; i < stoplist.size(); i++) {
                            BusStop busStop = new BusStop();
                            busStop.stopId = numlist.get(i);
                            busStop.stopName = stoplist.get(i);
                            stopList.add(busStop);
                        }
                    }
                    catch(IOException e) {
                        e.printStackTrace();
                    }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            super.onPostExecute(aVoid);
            switch(func) {
                case "getRouteAcctoThrghSttnList":

                case "getSttnAcctoArvlPrearngeInfoList" :

                case "getAllBusList" :
                    BusNameFragment.settingList(busList);
                case "getAllStopList" :
                    //BusNumFragment.settingList(stopList);
            }
        }
    }
}