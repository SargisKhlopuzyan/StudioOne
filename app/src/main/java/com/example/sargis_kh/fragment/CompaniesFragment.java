package com.example.sargis_kh.fragment;

        import android.os.Bundle;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentManager;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.ListView;

        import com.example.sargis_kh.adapter.CompaniesListAdapter;
        import com.example.sargis_kh.model.Company;
        import com.example.sargis_kh.studioone.R;

        import java.util.ArrayList;

public class CompaniesFragment extends Fragment {

    private static final String COMPANIES_FRAGMENT_LOG = "COMPANIES_FRAGMENT_LOG";

    private ArrayList<Company> companies;

    public CompaniesFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        Log.e(COMPANIES_FRAGMENT_LOG, "Companies onCreateView");

        View view = inflater.inflate(R.layout.fragment_companies, container, false);

        ListView listView = (ListView)view.findViewById(R.id.listview_companiesfragment);
        ArrayAdapter<Company> adapter = new CompaniesListAdapter(getContext(), companies);
        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager manager = getFragmentManager();
                Fragment frag = manager.findFragmentByTag("company_dialog_fragment_tag");
                if (frag != null) {
                    manager.beginTransaction().remove(frag).commit();
                }

                CompanyDialogFragment companyDialog = new CompanyDialogFragment(companies.get(position));
                companyDialog.show(manager, "company_dialog_fragment_tag");

                return true;
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
//        Log.e(COMPANIES_FRAGMENT_LOG, "Companies onResume");
    }

    public void setCompanies(ArrayList<Company> companies) {
        this.companies = companies;
    }
}
