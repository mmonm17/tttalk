package com.t_t_talk.DB.RemoteDB;

public interface Listener {
    void onSuccess(Response response);
    void onFailure(Response response);
}
