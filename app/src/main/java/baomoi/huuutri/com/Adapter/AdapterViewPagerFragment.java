package baomoi.huuutri.com.Adapter;

/**
 * Created by Thanh Trung on 29/05/2019.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import baomoi.huuutri.com.Fragment.Frm;

public class AdapterViewPagerFragment extends FragmentPagerAdapter {
    private int page_num;
    private Context context;

    private String[] tabTitles = new String[]{"Tin Nổi Bật","Pháp Luật", "Thế Giới" , "Thể Thao", "Xã Hội", "Văn Hóa", "Kinh Tế", "Công Nghệ", "Giải Trí"
            , "Giáo Dục", "Sức Khỏe", "Nhà Đất", "Xe Cộ"};
    private String URL1="";
    private String URL2="&page=0";
    private final int[] cateid=new int[]{0,8,1,6,2,3,4,5,7,9,10,11,12};
    public AdapterViewPagerFragment(Context context, FragmentManager fm, int page_num) {
        super(fm);
        this.context=context;
        this.page_num = page_num;
    }

    @Override
    public Fragment getItem(int position) {
        SharedPreferences preferences=context.getSharedPreferences("URL",Context.MODE_PRIVATE);
        URL1=preferences.getString("cate","");
        if(!URL1.equals("")) {
            switch (position) {

                case 0:
                    return Frm.newInstant(0, String.valueOf(cateid[0]),tabTitles[position], URL1 + cateid[0] + URL2);
                case 1:
                    return Frm.newInstant(1,String.valueOf(cateid[1]),tabTitles[position], URL1 + cateid[1] + URL2);
                case 2:
                    return Frm.newInstant(2, String.valueOf(cateid[2]),tabTitles[position], URL1 + cateid[2] + URL2);
                case 3:
                    return Frm.newInstant(3, String.valueOf(cateid[3]),tabTitles[position], URL1 + cateid[3] + URL2);
                case 4:
                    return Frm.newInstant(4, String.valueOf(cateid[4]),tabTitles[position], URL1 + cateid[4] + URL2);
                case 5:
                    return Frm.newInstant(5, String.valueOf(cateid[5]),tabTitles[position], URL1 + cateid[5] + URL2);
                case 6:
                    return Frm.newInstant(6, String.valueOf(cateid[6]),tabTitles[position], URL1 + cateid[6] + URL2);
                case 7:
                    return Frm.newInstant(7, String.valueOf(cateid[7]),tabTitles[position], URL1 + cateid[7] + URL2);
                case 8:
                    return Frm.newInstant(8, String.valueOf(cateid[8]),tabTitles[position], URL1 + cateid[8] + URL2);
                case 9:
                    return Frm.newInstant(9, String.valueOf(cateid[9]),tabTitles[position], URL1 + cateid[9] + URL2);
                case 10:
                    return Frm.newInstant(10, String.valueOf(cateid[10]),tabTitles[position], URL1 + cateid[10] + URL2);
                case 11:
                    return Frm.newInstant(11, String.valueOf(cateid[11]),tabTitles[position], URL1 + cateid[11] + URL2);
                case 12:
                    return Frm.newInstant(12, String.valueOf(cateid[12]),tabTitles[position], URL1 + cateid[12] + URL2);
                default:
                    return null;
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return page_num;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
