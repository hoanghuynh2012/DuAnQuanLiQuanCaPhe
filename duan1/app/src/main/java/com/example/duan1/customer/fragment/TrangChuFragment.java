package com.example.duan1.customer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.duan1.customer.Login.DangNhapActivity;
import com.example.duan1.R;
import com.example.duan1.customer.adapter.DanhChoBanAdapter;
import com.example.duan1.customer.adapter.TinTucAdapter;
import com.example.duan1.customer.model.DanhChoBan;
import com.example.duan1.customer.model.NguoiDungMailMode;
import com.example.duan1.customer.model.NguoiDungMode;
import com.example.duan1.customer.model.TinTuc;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class TrangChuFragment extends Fragment implements TinTucAdapter.ListItemClickListener {

    private ImageSlider slide;
    RecyclerView rcvTinTuc,rcvUuDai;
    Button btnDangNhap;
    List<TinTuc> list;
    List<DanhChoBan> list1;
    String role,key;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    StorageReference storageReference;
    public static  String nguoidangnhap;
    public static  String keyId;

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trang_chu,container,false);
        slide = view.findViewById(R.id.ImageSlide);
        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel
                ("https://file.hstatic.net/1000075078/file/go_green_1_a4bd9d34a2aa4b8e813dccff97553671_1024x1024.jpg","The Coffee House ?????ng h??nh c??ng b???n \"Go Green\"!"));
        slideModels.add(new SlideModel
                ("https://file.hstatic.net/1000075078/file/hinode_-_img_9322_e733cde7255641d0be8a31afc879b379_1024x1024.jpg","Top 10 c???a h??ng The Coffee House b???n n??n tr???i nghi???m t???i H?? N???i"));
        slideModels.add(new SlideModel
                ("https://file.hstatic.net/1000075078/file/freeupsize_vuong_b96b42170b864cecb3e4a5a0876229ce.jpg","7 NG??Y FREE UPSIZE - Y??U B???N N??? KH??NG TH??? N??O CAI"));
        slide.setImageList(slideModels,true);
        //Hooks
        rcvTinTuc = view.findViewById(R.id.my_recycler);
        rcvUuDai = view.findViewById(R.id.my_recycler2);

        rcvTinTuc.setHasFixedSize(true);
        list = new ArrayList<>();
        list.add(new TinTuc(R.drawable.s1, "V???i b??? ba tr??i c??y t???i THE COFFEE HOUSE \"Go Green\"!","N??m nay, The Coffee House mang l???i s??? ?????t ph?? trong h????ng v???" ));
        list.add(new TinTuc(R.drawable.s2, "Gi???m 25% cho kh??ch VinID","Tr?? ???i Thanh Long Macchiato s??? d???ng," +
                " tr?? hoa l??i nguy??n l?? h???o h???ng ph???i tr???n v???i n?????c ??p ???i th??m ng??t,"));
        list.add(new TinTuc(R.drawable.s3, "????nh g??i v??? h??? th???ng c???a h??ng","THE COFFEE HOUSE l?? m???t h??? th???ng c???a h??ng, chu???i CAF?? v???i kh??ng gian l???n, " +
                " t???i c??c v??? tr?? r???t ?????p t???i trung t??m c??c t???nh, th??nh ph???,"));
        list.add(new TinTuc(R.drawable.s4, "Kh??m ph?? tr?? ???i Macchiato","Tr?? ???i Thanh Long Macchiato s??? d???ng" +
                " tr?? hoa l??i nguy??n l?? h???o h???ng ph???i tr???n v???i n?????c ??p ???i th??m ng??t,"));

        TinTucAdapter adapter = new TinTucAdapter(getActivity(), (ArrayList<TinTuc>) list);

        rcvTinTuc.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rcvTinTuc.setAdapter(adapter);


        rcvUuDai.setHasFixedSize(true);
        list1 = new ArrayList<>();
        list1.add(new DanhChoBan(R.drawable.uudai1, "Gi???m 25% cho kh??ch VinID","T??? ng??y 16/03/2020 ??? 03/04/2020, m???i kh??ch s??? c?? c?? h???i nh???n ???????c voucher gi???m gi?? 25% c??c m??n n?????c khi ng?????i d??ng app VinID"));
        list1.add(new DanhChoBan(R.drawable.uudai2, "Kh??m ph?? tr?? ???i Macchiato ","Tr?? ???i Thanh Long Macchiato s??? d???ng " +
                "tr?? hoa l??i nguy??n l?? h???o h???ng ph???i tr???n v???i n?????c ??p ???i th??m ng??t," ));
        list1.add(new DanhChoBan(R.drawable.uudai3, "????nh g??i v??? h??? th???ng c???a h??ng","THE COFFEE HOUSE l?? m???t h??? th???ng c???a h??ng, chu???i CAF?? v???i kh??ng gian l???n, t???i c??c v??? tr?? r???t ?????p t???i trung t??m c??c t???nh, th??nh ph???"));
        list1.add(new DanhChoBan(R.drawable.uudai4, "S??? ??U ????I M???NG 300 QU??N","V???i 300 qu??n tr???i d???c ?????t n?????c," +
                " Highlands Coffee t??? h??o l?? n??i l??u gi??? tr??m chuy???n ?????m ???? c???a ng?????i Vi???t kh???p 3 mi???n"));
        list1.add(new DanhChoBan(R.drawable.uudai5, "\"?????i gi??\" V???i b??? ba tr??i c??y t???i THE COFFEE HOUSE","N??m nay, The Coffee House mang l???i s??? ?????t ph?? trong h????ng v???"));

        DanhChoBanAdapter adapter1 = new DanhChoBanAdapter(getActivity(), (ArrayList<DanhChoBan>) list1);

        rcvUuDai.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rcvUuDai.setAdapter(adapter1);

        btnDangNhap = view.findViewById(R.id.btnDangNhapTrangChu);
        btnDangNhap = view.findViewById(R.id.btnDangNhapTrangChu);
        try {
            role = getActivity().getIntent().getExtras().getString("role");
            key = getActivity().getIntent().getExtras().getString("key");
            if (role.equals("3")) {
                storageReference = FirebaseStorage.getInstance().getReference("NguoiDung");
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("NguoiDung").child(key);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        NguoiDungMode mode = snapshot.getValue(NguoiDungMode.class);
                        assert mode != null;
                        keyId = mode.getUserid();
                        btnDangNhap.setText(mode.getTennguoidung());
                        nguoidangnhap = mode.getTennguoidung();
                        btnDangNhap.setEnabled(false);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else if (role.equals("4")) {
                firebaseAuth = FirebaseAuth.getInstance();
                firebaseUser = firebaseAuth.getCurrentUser();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("NguoiDungMail").child(firebaseUser.getUid());
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        NguoiDungMailMode mode = snapshot.getValue(NguoiDungMailMode.class);
                        assert mode != null;
                        btnDangNhap.setText(mode.getEmail());
                        keyId = mode.getUserId();
                        nguoidangnhap = mode.getEmail();
                        btnDangNhap.setEnabled(false);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            } else if (role.equals("2")){
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("NguoiDung").child(key);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        NguoiDungMode mode = snapshot.getValue(NguoiDungMode.class);
                        btnDangNhap.setText(mode.getTennguoidung());
                        keyId = mode.getUserid();
                        nguoidangnhap = mode.getTennguoidung();
                        btnDangNhap.setEnabled(false);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            } else if (role.equals("1")){
                btnDangNhap.setText("Admin");
                btnDangNhap.setEnabled(false);
            }
        } catch (NullPointerException exception) {
            btnDangNhap.setText("????ng Nh???p");

        }
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), DangNhapActivity.class));

            }
        });

        super.onViewCreated(view, savedInstanceState);
        return view;
    }

    @Override
    public void onphoneListClick(int clickedItemIndex) {
    }


}