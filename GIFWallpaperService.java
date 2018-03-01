package com.diabin.tajmahallp.tajmahallivewallpaper;

import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;


/**
 * Created by risha on 11-11-2017.
 */

public class GIFWallpaperService extends WallpaperService {


    @Override
    public WallpaperService.Engine onCreateEngine() {
        try{
            Movie movie = Movie.decodeStream(getResources().getAssets().open("casette.gif"));
            return new GIFWallpaperEngine(movie);

        }catch (IOException e){
            Log.d("GIF","Could not load asset.");
            return null;
        }
    }

    private class GIFWallpaperEngine extends WallpaperService.Engine{
        private final int frameDuration = 25;
        private SurfaceHolder holder;
        private Movie movie;
        private Boolean visible;
        private Handler handler;

        public GIFWallpaperEngine (Movie movie){
            this.movie =movie;
            handler = new Handler();

        }

        public void onCreate(SurfaceHolder surfaceholder){
            super.onCreate(surfaceholder);
            this.holder = surfaceholder;

        }

        

        private Runnable drawGIF = new Runnable() {
            @Override
            public void run() {
                draw();
            }
        };

        private void draw(){
            if(visible){
                Canvas canvas = holder.lockCanvas();
                canvas.save();
                //Adjust Size and position of ur wallpaper
                    canvas.scale(3f,3f);
                    movie.draw(canvas, -100,0);
                canvas.restore();
                holder.unlockCanvasAndPost(canvas);
                movie.setTime((int) (System.currentTimeMillis() % movie.duration()));

                handler.removeCallbacks(drawGIF);
                handler.postDelayed(drawGIF,frameDuration);
            }
        }

        @Override
        public void onVisibilityChanged(boolean visible){
            this.visible = visible;
            if(visible){
                handler.post(drawGIF);

            }else{
                handler.removeCallbacks(drawGIF);
            }
        }

        @Override
        public void onDestroy(){
            super.onDestroy();
            handler.removeCallbacks(drawGIF);
        }

    }
}
