package baomoi.huuutri.com.Ads;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class Adbanner {
    private AdView adView;
    public Adbanner(AdView ad){
        this.adView=ad;
        AdRequest adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }
}
