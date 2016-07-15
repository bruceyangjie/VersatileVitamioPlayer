package io.vov.vitamio.demo.videoview;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import com.app.AppActivity;
import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnInfoListener;
import io.vov.vitamio.demo.R;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

public class VideoViewBuffer extends AppActivity implements OnInfoListener, OnBufferingUpdateListener {

  /**
   * TODO: Set the path variable to a streaming video URL or a local media file
   * path.
   */
  private String path = "http://video19.ifeng.com/video06/2012/04/11/629da9ec-60d4-4814-a940-997e6487804a.mp4";
  private Uri uri;
  private VideoView mVideoView;

  @Override public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    if (!LibsChecker.checkVitamioLibs(this)) return;
    setContentView(R.layout.videobuffer);
    mVideoView = (VideoView) findViewById(R.id.buffer);

    if (TextUtils.isEmpty(path)) {
      Toast.makeText(VideoViewBuffer.this, "Please edit VideoBuffer Activity, and set path" + " variable to your media file URL/path",
          Toast.LENGTH_LONG).show();
      return;
    } else {
      uri = Uri.parse(path);
      mVideoView.setVideoURI(uri);
      mVideoView.setMediaController(new MediaController(this));
      mVideoView.requestFocus();
      mVideoView.setOnInfoListener(this);
      mVideoView.setOnBufferingUpdateListener(this);
      mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
        @Override public void onPrepared(MediaPlayer mediaPlayer) {
          // optional need Vitamio 4.0
          mediaPlayer.setPlaybackSpeed(1.0f);
        }
      });
    }
  }

  @Override public boolean onInfo(MediaPlayer mp, int what, int extra) {
    switch (what) {
      case MediaPlayer.MEDIA_INFO_BUFFERING_START:
        if (mVideoView.isPlaying()) {
          mVideoView.pause();
          pb.setVisibility(View.VISIBLE);
          downloadRateView.setText("");
          loadRateView.setText("");
          downloadRateView.setVisibility(View.VISIBLE);
          loadRateView.setVisibility(View.VISIBLE);
        }
        break;
      case MediaPlayer.MEDIA_INFO_BUFFERING_END:
        mVideoView.start();
        pb.setVisibility(View.GONE);
        downloadRateView.setVisibility(View.GONE);
        loadRateView.setVisibility(View.GONE);
        break;
      case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
        downloadRateView.setText("" + extra + "kb/s" + "  ");
        break;
    }
    return true;
  }

  @Override public void onBufferingUpdate(MediaPlayer mp, int percent) {
    loadRateView.setText(percent + "%");
  }
}
