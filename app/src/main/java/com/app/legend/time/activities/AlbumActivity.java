package com.app.legend.time.activities;




import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.app.legend.time.R;
import com.app.legend.time.adapter.AlbumAdapter;
import com.app.legend.time.adapter.FolderAdapter;
import com.app.legend.time.bean.ImageInfo;
import com.app.legend.time.fragments.PreviewImageFragment;
import com.app.legend.time.interfaces.IAlbumActivity;
import com.app.legend.time.presenters.AlbumActivityPresenter;
import com.app.legend.time.utils.AlbumSpace;
import com.app.legend.time.utils.ImageUtils;
import java.io.Serializable;
import java.util.List;

/**
 * 选择照片
 */
public class AlbumActivity extends BaseActivity implements IAlbumActivity{

    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private AlbumAdapter adapter;
    private Toolbar toolbar;
    private LinearLayout bottomLayout;
    private TextView selText;
    private Button determine;
    private TextView showSelText;
    private LinearLayout folderLayout;
    private TextView imagePreview;

    private AlbumActivityPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        presenter=new AlbumActivityPresenter(this);

        getComponent();

        initToolbar();

        initList();

        click();

        slideHelper.setSlideActivity(AlbumActivity.this);//滑动返回

    }

    private void getComponent(){

        recyclerView=findViewById(R.id.image_select_list);
        toolbar=findViewById(R.id.album_toolbar);
        selText=findViewById(R.id.select_folder);
        determine=findViewById(R.id.determine);
        showSelText=findViewById(R.id.select_some);
        folderLayout=findViewById(R.id.select_folder_layout);
        imagePreview=findViewById(R.id.image_preview);
    }

    private void initToolbar(){

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorTeal));
        toolbar.setNavigationOnClickListener(v -> {

            finish();

        });

    }

    private void initList(){

        gridLayoutManager=new GridLayoutManager(this,3);
        adapter=new AlbumAdapter();
        adapter.setListener((items)->{

            String info=items+"/"+"9";

            showSelText.setText(info);

        });
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new AlbumSpace());
        getData();
    }

    private void getData(){

        presenter.getData(this);

    }

    @Override
    public void setData(List<ImageInfo> imageInfos) {
        adapter.setImageInfoList(imageInfos);
    }

    private void click(){

        determine.setOnClickListener(v -> {

            List<ImageInfo> imageInfoList=adapter.getDiaryInfoList();
            Intent intent=new Intent();
            intent.putExtra("image_list",(Serializable) imageInfoList);
            setResult(201,intent);

            finish();

        });


        /**
         * 选择文件夹事件
         */
        folderLayout.setOnClickListener(v -> {

            showFolderList();
        });

        /**
         * 点击预览
         */
        imagePreview.setOnClickListener(v -> {

            PreviewImageFragment fragment=new PreviewImageFragment();

            Bundle bundle=new Bundle();
            List<ImageInfo> imageInfoList=adapter.getDiaryInfoList();
            bundle.putSerializable("list", (Serializable) imageInfoList);

            fragment.setArguments(bundle);
            addFragment(fragment,R.id.fragment_layout);



//            fragment.setImageInfoList(imageInfoList);


        });

    }

    private void showFolderList(){


        View view= LayoutInflater.from(AlbumActivity.this).inflate(R.layout.bottom_sheet_dialog_content_view,null,false);

        RecyclerView recyclerView=view.findViewById(R.id.bottom_list);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(AlbumActivity.this);

        FolderAdapter adapter=new FolderAdapter();

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
//        adapter.setMusicPositionList(PlayHelper.create().getCurrentMusicList());
        List<String> folderList= ImageUtils.getFolderList(this.adapter.getImageInfoList());

        folderList.add(0,"全部");
        adapter.setFolderList(folderList);

        BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(AlbumActivity.this);

        bottomSheetDialog.setContentView(view);

        adapter.setListener(name -> {

            this.adapter.setFolder(name);

            bottomSheetDialog.cancel();

            name=name.substring(name.lastIndexOf("/")+1,name.length());

            this.selText.setText(name);

        });

//        view.setBackgroundColor(getResources().getColor(R.color.colorBlue));
//        bottomSheetDialog.getDelegate()
//                .findViewById(android.support.design.R.id.design_bottom_sheet)
//                .setBackgroundColor(getResources().getColor(R.color.colorTransparent));

//        View v=bottomSheetDialog.getDelegate()
//                .findViewById(android.support.design.R.id.design_bottom_sheet);
//        if (v!=null) {
//            v.setBackground(getResources().getDrawable(R.drawable.shape_bottom, getTheme()));
//
//        }

        bottomSheetDialog.show();

    }

    public void setToolbar(Toolbar toolbar){

        setSupportActionBar(toolbar);
        toolbar.setFitsSystemWindows(true);

    }

    private void addFragment(Fragment fragment, int res){

        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();

        transaction.add(res,fragment);

        transaction.addToBackStack(null);

        transaction.commit();


    }

    public void removeFragment(Fragment fragment){

        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.remove(fragment);

        fragmentManager.popBackStack();//移除栈
        transaction.commit();

    }
}
