package com.farmerfirst.growagric.ui.account;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.farmerfirst.growagric.App;
import com.farmerfirst.growagric.R;
import com.farmerfirst.growagric.databinding.FragmentAccountBinding;
import com.farmerfirst.growagric.datastore.LocalData;
import com.farmerfirst.growagric.datastore.LocalSharedPreferences;
import com.farmerfirst.growagric.ui.farm.recyclerview.FarmAndroidViewModel;
import com.farmerfirst.growagric.ui.farm.recyclerview.FarmRecyclerViewActivity;
import com.farmerfirst.growagric.ui.farm_visit.FarmVisitsActivity;
import com.farmerfirst.growagric.ui.finance.recyclerview.FinanceAndroidViewModel;
import com.farmerfirst.growagric.ui.finance.recyclerview.FinanceRecyclerViewActivity;
import com.farmerfirst.growagric.ui.invite.InviteActivity;
import com.farmerfirst.growagric.ui.login.LoginActivity;
import com.farmerfirst.growagric.ui.profile.ProfileActivity;
import com.farmerfirst.growagric.ui.terms.CustomDialog;
import com.farmerfirst.growagric.utils.ItemArrayAdapter;
import com.farmerfirst.growagric.utils.dojo.Item;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class AccountFragment extends Fragment {

    private FragmentAccountBinding binding;
    private ListView listView;
    private static final int PICK_IMAGE_REQUEST = 1;

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        AccountViewModel accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);

        binding = FragmentAccountBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        LocalSharedPreferences prefs = new LocalSharedPreferences(getActivity());

        displayUserName(prefs);

        getFarmFinanceCount(prefs);

        setupDashboard();

        ArrayList<Item> datamodels = new ArrayList<>();
        datamodels.add(new Item(R.drawable.ic_profile128x128,"View profile",""));
        datamodels.add(new Item(R.drawable.ic_invite128x128,"Invite a farmer",""));
        //datamodels.add(new Item(R.drawable.ic_invite128x128,"Waiting list",""));
        datamodels.add(new Item(R.drawable.ic_legal128x128,"Legal",""));
        //datamodels.add(new Item(R.drawable.ic_empty128x128,"Farm visit",""));
        datamodels.add(new Item(R.drawable.ic_logout_128x128,"Logout",""));

        ItemArrayAdapter mItemAdapter = new ItemArrayAdapter(Objects.requireNonNull(getActivity()),datamodels);
        binding.listProfileItems.setAdapter(mItemAdapter);

        binding.listProfileItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (datamodels.get(position).getName().toLowerCase().toString()){
                    case "view profile":
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(getActivity(),ProfileActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                startActivity(intent);
                            }
                        },100);
                    break;
                    case "invite a farmer":
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent v = new Intent(getActivity(), InviteActivity.class);
                                v.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                startActivity(v);
                            }
                        },100);
                    break;
                    case "waiting list":
                        Toast.makeText(getContext(),"Under construction",Toast.LENGTH_SHORT).show();
                    break;
                    case "legal":
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                CustomDialog.show(getContext());
                            }
                        },100);
                    break;
                    case "farm visit":
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent i = new Intent(getActivity(), FarmVisitsActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                startActivity(i);
                            }
                        },100);
                    break;
                    case "logout":
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                LocalSharedPreferences prefs = new LocalSharedPreferences(getContext());
                                prefs.setIsLogin(false);
                                prefs.deleteTrackProfileInfoUpdate();
                                //prefs.deleteProfileInfo();
                                Intent k = new Intent(getActivity(),LoginActivity.class);
                                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                getActivity().startActivity(k);
                                getActivity().finish();
                            }
                        },100);
                    break;
                }
            }
        });

        return root;
    }

    private void setupDashboard(){
        binding.farmCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(getActivity(), FarmRecyclerViewActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(i);
                    }
                },100);
            }
        });

        binding.financeCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent j = new Intent(getActivity(), FinanceRecyclerViewActivity.class);
                        j.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(j);
                    }
                },100);
            }
        });

        binding.ivProfileAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pickImageFromGallery();
                    }
                },100);
            }
        });

        binding.linearLayoutAddProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pickImageFromGallery();
                    }
                },100);
            }
        });
    }

    private void displayUserName(LocalSharedPreferences prefs){
        try{
            String data = prefs.getProfileInfo();
            JSONObject obj = new JSONObject(data);
            binding.tvUserProfileName.setText(obj.getJSONObject("message").get("first_name").toString() + " " +obj.getJSONObject("message").get("last_name").toString());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void getFarmFinanceCount(LocalSharedPreferences prefs){
        FarmAndroidViewModel farmAndroidViewModel = new ViewModelProvider(getActivity()).get(FarmAndroidViewModel.class);
        binding.tvFarmCount.setText(""+farmAndroidViewModel.getFarmCNT(LocalData.GetUserUUID(prefs)));
        FinanceAndroidViewModel financeAndroidViewModel = new ViewModelProvider(getActivity()).get(FinanceAndroidViewModel.class);
        binding.tvFinanceCount.setText(""+financeAndroidViewModel.getFinanceCNT(LocalData.GetUserUUID(prefs)));

        String path = prefs.getAvatar();
        if(!path.equals("")) {
            Glide.with(App.getContext()).load(Uri.parse(path)).into(binding.ivProfileAvatar);
        }else{
            Glide.with(App.getContext()).load(R.drawable.dash_profile128x128).into(binding.ivProfileAvatar);
        }
    }

    private void pickImageFromGallery() {
        try {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            LocalSharedPreferences prefs = new LocalSharedPreferences(App.getContext());
            Uri selectedImageUri = data.getData();
            if(!prefs.getAvatar().equals("")){
                prefs.deleteAvatar();
            }

            prefs.setAvatar(selectedImageUri);//ivProfileAvatar
            Glide.with(App.getContext()).load(selectedImageUri).into(binding.ivProfileAvatar);

            /*
            RequestBody user_uuid = RequestBody.create(MediaType.parse("text/plain"), "uuid");
            RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "name");
            RequestBody description = RequestBody.create(MediaType.parse("text/plain"), "description");
            RequestBody action = RequestBody.create(MediaType.parse("text/plain"), "profile");

            System.out.println("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZ  "+ Utils.getRealPathFromURI(selectedImageUri));

            String path = Utils.getRealPathFromURI(selectedImageUri);
            UploadImageWithData uploadImageWithData = new UploadImageWithData(user_uuid, name, description, action);

            uploadImageWithData.HttpPost(new File(path), new ICustomResponseCallback() {
                @Override
                public void onSuccess(String value) {
                    System.out.println("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ " + value);
                }
                @Override
                public void onFailure() {
                }
            });
            */
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}