package ust.cytangah.coursecat;

import ust.cytangah.coursecat.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.actionbarsherlock.view.Window;


public class Detail extends SherlockFragmentActivity{
    public static final String TAB_1 = "Section";
    public static final String TAB_2 = "Catalog Entry";
    public static String code;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //Set b4 setContentView 
        //Window.class in com.actionbarsherlock.view and NOT android.view
        this.setTitle(RegCourse.SELECTED_SEM);
        setContentView(R.layout.detail);

        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        Bundle extras = getIntent().getExtras();
        
        if (extras != null) {
        	code = extras.getString("code");
        }
       
        //Create two fragments
        final Section section = new Section();
        final CatalogEntry catalogEntry = new CatalogEntry();
        
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        
        actionBar.addTab(actionBar.newTab().setText("Section").setTabListener(new TabListener(section)));
        actionBar.addTab(actionBar.newTab().setText("Catalog Entry").setTabListener(new TabListener(catalogEntry)));
    }

    public class TabListener implements ActionBar.TabListener {
        private final SherlockListFragment mFragment;

        public TabListener(SherlockListFragment fragment) {
            this.mFragment = fragment;
        }

        public void onTabSelected(Tab tab, FragmentTransaction ft) {
            ft.replace(R.id.fragment_container, mFragment, null);
        }

        public void onTabUnselected(Tab tab, FragmentTransaction ft) {

        }

        public void onTabReselected(Tab tab, FragmentTransaction ft) {
            // do nothing
        }
    }
}