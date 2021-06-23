package com.videoteca.student.ui.curso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.videoteca.student.data.model.realm.CursoData;
import com.videoteca.student.data.model.realm.SessionData;
import com.videoteca.student.data.model.realm.VideoData;
import com.videoteca.student.data.model.retrofit.Video;
import com.videoteca.student.managers.SessionManager;
import com.videoteca.student.ui.adapters.VideoAdapter;
import com.videoteca.student.ui.video.VideoActivity;
import com.videoteca.student.R;

import java.util.List;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class CursoActivity extends AppCompatActivity {

    private RecyclerView videoRecyclerView;
    private VideoAdapter videoAdapter;
    //private ArrayList<Video> videos;
    private RealmList<VideoData> videos;
    private Context context;
    private CursoViewModel cursoViewModel;
    //Session manager
    private SessionManager sessionManager;
    //Start Realm Object
    private Realm realm;
    private String authorization;
    private RealmResults videoResults;
    private int id_curso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curso);

        cursoViewModel = new ViewModelProvider(this, new CursoViewModelFactory()).get(CursoViewModel.class);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        context = this;
        Intent intent = getIntent();

        Bundle data = intent.getExtras();

        if (data != null) {
            Gson gson = new Gson();
            CursoData cursoData = gson.fromJson(data.getString("CURSO_JSON"), CursoData.class);
            getSupportActionBar().setTitle(cursoData.getNombre());
            id_curso = cursoData.getId();
        } else {
            finish();
        }

        // Instance Session Manager
        sessionManager = new SessionManager(getApplicationContext());

        //Get realm instance
        realm = Realm.getDefaultInstance();

        videoRecyclerView = findViewById(R.id.video_recycler_view);

        //Get data session Realm
        SessionData sessionData = realm.where(SessionData.class).findFirst();
        Log.i("Curso Video User Name", sessionData.getNombre().concat(" ").concat(sessionData.getPrimer_ap()).concat(" ").concat(sessionData.getSegundo_ap()));
        Log.i("Curso Video User Email", sessionData.getEmail());
        authorization = sessionData.getToken();

        // sections = prepareData();
        //videos = prepareData();

        videos = new RealmList<>();

        // Recuperando los videos de los cursos
        RealmQuery<VideoData> queryVideos = realm.where(VideoData.class);
        videoResults = queryVideos.equalTo("id_curs", id_curso).findAll();

        if (videoResults.size() > 0){
            Log.e("Videos Results Size", videoResults.size()+"");
            Log.e("videos.size()", videos.size()+"");
            List<VideoData> videosAux = realm.copyFromRealm(videoResults);
            videos.addAll(videosAux);

        }

        if (videoResults.size() == 0){
            cursoViewModel.getVideos(this, authorization);
        }

        videoAdapter = new VideoAdapter(context, videos);
        videoAdapter.setOnItemClickListener(new VideoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(VideoData videoData) {
                //Toast.makeText(context, video.getTitulo(), Toast.LENGTH_LONG).show();

                //Gson gson = new Gson();
                String videoJSON = new Gson().toJson(videoData);

                Intent intent = new Intent(context, VideoActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("VIDEO_JSON", videoJSON);
                context.startActivity(intent);

            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false);
        videoRecyclerView.setLayoutManager(gridLayoutManager);
        videoRecyclerView.setAdapter(videoAdapter);

        /*cursoViewModel.getVideosData().observe(this, new Observer<RespVideos>() {
            @Override
            public void onChanged(RespVideos respVideos) {
                prepareVideos(respVideos);
            }
        });*/
        cursoViewModel.getListVideoData().observe(this, new Observer<List<Video>>() {
            @Override
            public void onChanged(List<Video> videos) {
                prepareVideos(videos);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //this.finish();
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void prepareVideos(List<Video> respVideoList) {

        // Comprobando registro de videos
        if (videoResults.size() > 0) {
            realm.beginTransaction();
            realm.delete(VideoData.class);
            realm.commitTransaction();

            videoResults.deleteAllFromRealm();
        }

        if (videos.size() > 0) {
            videos.clear();
            videoAdapter.notifyDataSetChanged();
        }

        List<Video> videoList = respVideoList;

        realm.beginTransaction();

        RealmList<VideoData> videoDataX = new RealmList<>();
        for (Video video: videoList) {
            Log.i("Video ID", String.valueOf(video.getId()));
            Log.i("Video Titulo", video.getTitulo());
            videoDataX.add(new VideoData(video.getId(), video.getTitulo(), video.getDescripcion(), video.getPathVideo(), video.getNameVideo(), video.getDuracion(), video.getPeso(), video.getEstado(),video.getIdUsu(), video.getIdCurs(), video.getCreatedAt(), video.getUpdatedAt()));
            //videoDataX.add(new VideoData(video.getId(), video.getTitulo(), "", "", "", "", "", 1,1,1, "", ""));
        }

        realm.copyToRealmOrUpdate(videoDataX);
        realm.commitTransaction();


        RealmQuery<VideoData> queryVideosAux = realm.where(VideoData.class);
        RealmResults videoResultsAux = queryVideosAux.equalTo("id_curs", id_curso).findAll();
        List<VideoData> videosAux = realm.copyFromRealm(videoResultsAux);
        videos.addAll(videosAux);

        //videos.addAll(videoDataX);
        videoAdapter.notifyDataSetChanged();


    }

    private void loadVideos() {
        RealmQuery<VideoData> queryVideosAux = realm.where(VideoData.class);
        videoResults = queryVideosAux.equalTo("id_curs", id_curso).findAll();
        List<VideoData> videosAux = realm.copyFromRealm(videoResults);
        videos.addAll(videosAux);
        //videoAdapter.notifyDataSetChanged();
    }

    /*private ArrayList<Video> prepareData() {
        ArrayList<Video> videos = new ArrayList<>();

        videos.add(new Video(1, "Primera clase de geometria", "Entramos a detalle", "","", "",true, 1, 1));
        videos.add(new Video(2, "Segunda clase de trigonometria", "Entramos a detalle", "","", "",true, 1, 1));
        videos.add(new Video(3, "Tercera clase de potencia", "Entramos a detalle", "","", "",true, 1, 1));
        videos.add(new Video(4, "Cuarta clase de geometria", "Entramos a detalle", "","", "",true, 1, 1));

        return videos;
    }*/

}