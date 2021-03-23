package e.user.gumibusremake;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BusNameFragment extends Fragment {

    private static List<String> list;          // 데이터를 넣은 리스트변수
    private ListView listView;          // 검색을 보여줄 리스트변수
    private EditText editSearch;        // 검색어를 입력할 Input 창
    private SearchAdapter adapter;      // 리스트뷰에 연결할 어댑터
    private ArrayList<String> arraylist;
    private static ArrayList<Bus> busList;
    private static TaskManager tm = new TaskManager();

    public BusNameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bus_name, container, false);

        editSearch = (EditText) view.findViewById(R.id.searchName);
        listView = (ListView) view.findViewById(R.id.listView);
        list = new ArrayList<String>();

        try {
            tm.getAllBusList();
        } catch (IOException e) {
            e.printStackTrace();
        }

        arraylist = new ArrayList<String>();
        arraylist.addAll(list);

        System.out.println("ArrayList : " + arraylist.size());
        // 리스트에 연동될 어댑터를 생성한다.
        adapter = new SearchAdapter(list, this.getContext());//getActivity 수정함
        // 리스트뷰에 어댑터를 연결한다.
        listView.setAdapter(adapter);

        // input창에 검색어를 입력시 "addTextChangedListener" 이벤트 리스너를 정의한다.
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // input창에 문자를 입력할때마다 호출된다.
                // search 메소드를 호출한다.
                String text = editSearch.getText().toString();
                if(arraylist.size()==0)
                    arraylist.addAll(list);

                search(text);
            }
        });
        return view;
    }
    public void search(String charText) {


        System.out.println("list size : " + list.size());
        // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        list.clear();
        System.out.println("list size : " + list.size());
        // 문자 입력이 없을때는 모든 데이터를 보여준다.
        if (charText.length() == 0) {
            list.addAll(arraylist);
        }
        // 문자 입력을 할때..
        else
        {
            // 리스트의 모든 데이터를 검색한다.
            for(int i = 0;i < arraylist.size(); i++)
            {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (arraylist.get(i).toLowerCase().contains(charText))
                {
                    // 검색된 데이터를 리스트에 추가한다.
                    list.add(arraylist.get(i));
                }
            }
        }
        // 리스트 데이터가 변경되었으므로 어댑터를 갱신하여 검색된 데이터를 화면에 보여준다.
        adapter.notifyDataSetChanged();
    }
    public static void settingList(ArrayList<Bus> tmpList){

        busList = tmpList;

        for(int i = 0;i<354;i++)
        {
            list.add(busList.get(i).busNo);
        }
    }
    public static BusNameFragment newInstance() {

        Bundle args = new Bundle();

        BusNameFragment fragment = new BusNameFragment();
        fragment.setArguments(args);
        return fragment;
    }
}