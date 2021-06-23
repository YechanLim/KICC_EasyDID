package com.example.easydid.ui.fragment;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.easydid.EasyDidApplication;
import com.example.easydid.R;
import com.example.easydid.databinding.FragmentEasyvideoBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class EasyVideoFragment extends Fragment {

    private FragmentEasyvideoBinding binding;
    private EasyVideoViewModel viewModel;

    private final String videoFilePath = Environment.getExternalStorageDirectory() + "/easypos_download/";
//    "storage/emulated/0/easypos/dat/"
    private final String imgFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/easydid/dat/";

    private List<String> videoFileList;
    private int videoFileIndex;
    private int videoFileCount;

    private List<String> imageFileList;
    private int imageFileIndex;
    private int imageFileCount;

    private VideoView videoView;

    private Timer mIdleTimer;

    protected SharedPreferences prefs = EasyDidApplication.getInstance().getPrefs();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(EasyVideoViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_easyvideo, container, false);
        View root = binding.getRoot();
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);
        
        initView();
        
        return root;
    }

    public void initView(){
        videoFileIndex = 0;
        imageFileIndex = 0;
        videoFileCount = getVideoFileList();
        imageFileCount = getImageFileList();

        if(videoFileCount != 0){
            binding.videoView.setVideoPath(videoFileList.get(videoFileIndex));
            binding.videoView.requestFocus();
            binding.videoView.start();
            binding.videoView.setOnCompletionListener(mediaPlayer -> {
                videoFileIndex++;
                if(videoFileIndex == videoFileCount){
                    videoFileIndex = 0;
                }

                binding.videoView.setVideoPath(videoFileList.get(videoFileIndex));
                binding.videoView.requestFocus();
                binding.videoView.start();
            });
        }
    }

    /**
     * 듀얼동영상 zip파일에 동영상 압축파일 업로드
     *
     * @return
     */
    private int getVideoFileList() {

        videoFileList = new ArrayList<>();

        File dirFile = new File(videoFilePath);
        File[] fileList = dirFile.listFiles();

        int fileCount = 0;
        for (File file : fileList) {
            if (file.isFile() && isVideoFile(file.getName())) {
                videoFileList.add(videoFilePath + file.getName());
                fileCount++;
            }
        }
        return fileCount;
    }

    /**
     * 듀얼동영상 zip파일에 이미지 압축파일 업로드
     *
     * @return
     */
    private int getImageFileList() {

        imageFileList = new ArrayList<>();
        int fileCount = 0;

        File dirFile = new File(imgFilePath);
        if (dirFile.exists()) {
            File[] fileList = dirFile.listFiles();
            if (fileList != null) {
                for (File file : fileList) {
                    if (file.isFile() && isImageFile(file.getName())) {
                        // imageFileList.add(Constants.FILE_IMAGE_PATH + file.getName());
                        imageFileList.add(imgFilePath + file.getName());
                        fileCount++;
                    }
                }
            }
        }
        return fileCount;
    }

    private boolean isVideoFile(String filename) {
        String[] extensions = {"mp4", "mpg", "mpeg", "swf", "avi"};

        for (int i = 0; i < extensions.length; i++) {
            if (filename.contains(extensions[i])) {
                return true;
            }
        }

        return false;
    }

    private boolean isImageFile(String filename) {
        String[] extensions = {"jpg", "png"};

        for (int i = 0; i < extensions.length; i++) {
            if (filename.contains(extensions[i])) {
                return true;
            }
        }

        return false;
    }

}
