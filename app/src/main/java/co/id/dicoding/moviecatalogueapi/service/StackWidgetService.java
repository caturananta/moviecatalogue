package co.id.dicoding.moviecatalogueapi.service;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

import co.id.dicoding.moviecatalogueapi.widgets.StackRemoteViewsFactory;

public class StackWidgetService extends RemoteViewsService {
    private final String TAG = StackWidgetService.class.getSimpleName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.e(TAG, "onGetViewFactory: start" );
        return new StackRemoteViewsFactory(this.getApplicationContext());
    }
}
