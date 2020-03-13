package com.codingnew.rentbuysell;

import com.codingnew.rentbuysell.Notification.Myresponse;
import com.codingnew.rentbuysell.Notification.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIservices {
    @Headers(
            {"Content-Type:application/json",
            "Authorization:key=AAAAaNwEVdo:APA91bGJAEDteYgvTowqwqLqz_VnxA6ILBPNkvUiq9mt301Jb4cCH_voKpn-A9tGCvZZyw6BYwCR91Y2RnXGDBX6aWO1Gb865R_2stVWPHh75X0VONOmngKPHeYscC2tls-Y8WtyITql"
            }

    )
    @POST("fcm/send")
    Call<Myresponse> sendNotification(@Body Sender body);

}
