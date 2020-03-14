package baomoi.huuutri.com.Ads;

import android.content.Context;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import baomoi.huuutri.com.R;

public class Adfull {
    InterstitialAd mInterstitialAd;
    Context context;

    public Adfull(final Context context) {
        this.context = context;
        mInterstitialAd = new InterstitialAd(context);


        mInterstitialAd.setAdUnitId(context.getString(R.string.ad_full));

        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mInterstitialAd.loadAd(adRequest);

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {

            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                Common.interstitialAd = (new Adfull(context)).getAd();
            }
        });
    }

    public InterstitialAd getAd(){
        return mInterstitialAd;
    }
}
