package com.farmerfirst.growagric.ui.farm_visit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.DatePickerDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.DatePicker;

import com.farmerfirst.growagric.R;
import com.farmerfirst.growagric.databinding.ActivityFarmVisitsBinding;
import com.farmerfirst.growagric.ui.softkeyboardadjust.AndroidBug5497Workaround;
import com.farmerfirst.growagric.utils.ComponentUtils;
import com.farmerfirst.growagric.utils.dojo.CustomAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class FarmVisitsActivity extends AppCompatActivity{
    private ActivityFarmVisitsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(FarmVisitsActivity.this,R.layout.activity_farm_visits);

        ComponentUtils.customActionBar("Farm visit", FarmVisitsActivity.this);

        AndroidBug5497Workaround.assistActivity(FarmVisitsActivity.this);

        initSpinnerVisitOne(getResources());
        initCalendarVisitOne();
        radioButtonManagmentVisitOne();
        checkBoxManagementVisitOne();

        initCalendarVisitTwo();
        radioButtonManagementVisitTwo();

        initCalendarVisitThree();
        radioButtonManagementVisitThree();

        binding.farmVisitBeforeFinance.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //-.search a farmer.
                String farmerName = binding.editSearchFarmer.getText().toString();
                String dateOfVisit = binding.farmVisitBeforeFinance.editDateOfVisit.getText().toString();
                String materialChickenHouseIsMadeOf = binding.farmVisitBeforeFinance.editMaterialChickenHouseMadeOf.getText().toString();
                String noOfChickenWeShouldFinance = binding.farmVisitBeforeFinance.editNumberOfChickenShouldWeFinance.getText().toString();
                String loanAmount = binding.farmVisitBeforeFinance.editFinanceFarmerIsSeeking.getText().toString();
                String dateFarmerStartsFarming = binding.farmVisitBeforeFinance.editDateFarmingCanStart.getText().toString();
                String currentFarmInsurer = binding.farmVisitBeforeFinance.editInsurerName.getText().toString();
                //-.no of bird that can fit in the current house.
                String noOfChickThatCanFitInCurrentHouse = binding.farmVisitBeforeFinance.editNumberOfChickenThatFitInCurrentHouse.getText().toString();
                String farmerPostal = binding.farmVisitBeforeFinance.editFarmerPostalBox.getText().toString();
            }
        });

        binding.farmVisitAfterFinance.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //-.visit 2.
                String noOfChildrenThatLiveWithFamily = binding.farmVisitAfterFinance.editNumberOfChildrenInFamily.getText().toString();
                String noOfChildrenUnder18 = binding.farmVisitAfterFinance.editNumberOfChildrenUnderAge18.getText().toString();
                String noOfPeopleLiveInHouse = binding.farmVisitAfterFinance.editNumberOfPeopleLiveInTheHouse.getText().toString();
                //binding.farmVisitAfterFinance.spinMaterialFarmersHouseMadeOf
                String noOfChickenHaveWeFinanced = binding.farmVisitAfterFinance.editNumberOfChickenWeHaveFinanced.getText().toString();
                String noOfPeopleWorkAtFarm = binding.farmVisitAfterFinance.editNumberOfEmployeesAtFarm.getText().toString();
                String noOfExtraChicken = binding.farmVisitAfterFinance.editNumberOfChicksFarmerReceived.getText().toString();
                String noOfChickenThatOnDelivery = binding.farmVisitAfterFinance.editNumberOfChicksThatDiedUponDelivery.getText().toString();
                String adviceToFarmer = binding.farmVisitAfterFinance.editAdviceToFarmer.getText().toString();
                String nextVisitDate = binding.farmVisitAfterFinance.editDateOfNextVisit.getText().toString();
            }
        });

        binding.farmReturnVisitAfterFinance.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noOfChickenThatDiedSinceLastVisit = binding.farmReturnVisitAfterFinance.editNumberOfChickThatHaveDiedSinceLastVisit.getText().toString();
                String totalNoOfChickThatHaveDied = binding.farmReturnVisitAfterFinance.editTotalNumberOfChickenThatHaveDied.getText().toString();
                String additionalObservation = binding.farmReturnVisitAfterFinance.editAdditionalObservation.getText().toString();
                String dateOfNextVisit = binding.farmReturnVisitAfterFinance.editDateOfNextVisit.getText().toString();
            }
        });
        //-.select farm.
        binding.spinFarmDetail.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //-.other animals kept in the farm.
    }
    private void radioButtonManagmentVisitOne(){
        //-.do we have farm records.
        binding.farmVisitBeforeFinance.hasPreviousRecordYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    binding.farmVisitBeforeFinance.radioGrpTakenPhotosOfRecords.setVisibility(View.VISIBLE);
                }
            }
        });
        binding.farmVisitBeforeFinance.hasPreviousRecordNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    binding.farmVisitBeforeFinance.radioGrpTakenPhotosOfRecords.setVisibility(View.GONE);
                }
            }
        });
        //-.taken photos of documents.
        binding.farmVisitBeforeFinance.haveTakenPhotoOfRecordYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    binding.farmVisitBeforeFinance.textTakenPhotosOfRecords.setVisibility(View.VISIBLE);
                    binding.farmVisitBeforeFinance.buttonUploadFarmRecord.setVisibility(View.VISIBLE);
                }
            }
        });
        binding.farmVisitBeforeFinance.haveTakenPhotoOfRecordNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    binding.farmVisitBeforeFinance.textTakenPhotosOfRecords.setVisibility(View.GONE);
                    binding.farmVisitBeforeFinance.buttonUploadFarmRecord.setVisibility(View.GONE);
                }
            }
        });
        //-.material the bird house is made of.
        binding.farmVisitBeforeFinance.radioButtonStone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    binding.farmVisitBeforeFinance.texMaterialChickenHouseMadeOf.setVisibility(View.GONE);
                }
            }
        });
        binding.farmVisitBeforeFinance.radioButtonMabati.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    binding.farmVisitBeforeFinance.texMaterialChickenHouseMadeOf.setVisibility(View.GONE);
                }
            }
        });
        binding.farmVisitBeforeFinance.radioButtonWireMesh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    binding.farmVisitBeforeFinance.texMaterialChickenHouseMadeOf.setVisibility(View.GONE);
                }
            }
        });
        binding.farmVisitBeforeFinance.radioButtonOther.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    binding.farmVisitBeforeFinance.texMaterialChickenHouseMadeOf.setVisibility(View.VISIBLE);
                }
            }
        });
        //-.has insurance.
        binding.farmVisitBeforeFinance.buttonInsuranceYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    binding.farmVisitBeforeFinance.textInsurerName.setVisibility(View.VISIBLE);
                    binding.farmVisitBeforeFinance.radioGrpHasEvidenceOfInsuranceCover.setVisibility(View.VISIBLE);
                    binding.farmVisitBeforeFinance.radioGrpInsuranceFormFilled.setVisibility(View.GONE);
                }
            }
        });
        binding.farmVisitBeforeFinance.buttonInsuranceNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    binding.farmVisitBeforeFinance.textInsurerName.setVisibility(View.GONE);
                    binding.farmVisitBeforeFinance.radioGrpHasEvidenceOfInsuranceCover.setVisibility(View.GONE);
                    binding.farmVisitBeforeFinance.radioGrpInsuranceFormFilled.setVisibility(View.VISIBLE);
                }
            }
        });
        //-.evidence of insurance.
        binding.farmVisitBeforeFinance.buttonInsuranceEvidenceYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    binding.farmVisitBeforeFinance.textUploadInsuranceEvidence.setVisibility(View.VISIBLE);
                    binding.farmVisitBeforeFinance.buttonUploadInsuranceEvidence.setVisibility(View.VISIBLE);
                }
            }
        });
        binding.farmVisitBeforeFinance.buttonInsuranceEvidenceNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    binding.farmVisitBeforeFinance.textUploadInsuranceEvidence.setVisibility(View.GONE);
                    binding.farmVisitBeforeFinance.buttonUploadInsuranceEvidence.setVisibility(View.GONE);
                }
            }
        });
        binding.farmVisitBeforeFinance.buttonInsuranceEvidenceNotAcceptable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    binding.farmVisitBeforeFinance.textUploadInsuranceEvidence.setVisibility(View.GONE);
                    binding.farmVisitBeforeFinance.buttonUploadInsuranceEvidence.setVisibility(View.GONE);
                }
            }
        });
        //-.filled cic form correctly.
        binding.farmVisitBeforeFinance.buttonInsuranceFilledYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    binding.farmVisitBeforeFinance.radioGrpGivenFarmerCICFormToFill.setVisibility(View.GONE);
                }
            }
        });
        binding.farmVisitBeforeFinance.buttonInsuranceFilledNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    binding.farmVisitBeforeFinance.radioGrpGivenFarmerCICFormToFill.setVisibility(View.VISIBLE);
                }
            }
        });
        //-.has farmer been given cic form to fill.
        binding.farmVisitBeforeFinance.buttonGivenFarmerCicFormYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    binding.farmVisitBeforeFinance.textGivenFarmerCICFormMessage.setVisibility(View.GONE);
                }
            }
        });
        binding.farmVisitBeforeFinance.buttonGivenFarmerCicFormNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    binding.farmVisitBeforeFinance.textGivenFarmerCICFormMessage.setVisibility(View.VISIBLE);
                }
            }
        });
        //-.has farmer obtained stamp vet report.
        binding.farmVisitBeforeFinance.buttonVetStampedReportYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    binding.farmVisitBeforeFinance.textVetStampedReportMessage.setVisibility(View.GONE);
                }
            }
        });
        binding.farmVisitBeforeFinance.buttonVetStampedReportNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    binding.farmVisitBeforeFinance.textVetStampedReportMessage.setVisibility(View.VISIBLE);
                }
            }
        });
        //-.obtain farmers doc pin or id.
        binding.farmVisitBeforeFinance.buttonObtainIDCopiesYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    binding.farmVisitBeforeFinance.linearUploadIDPinCert.setVisibility(View.VISIBLE);
                    binding.farmVisitBeforeFinance.buttonUploadIDPIN.setVisibility(View.VISIBLE);
                }
            }
        });
        binding.farmVisitBeforeFinance.buttonObtainIDCopiesNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    binding.farmVisitBeforeFinance.linearUploadIDPinCert.setVisibility(View.GONE);
                    binding.farmVisitBeforeFinance.buttonUploadIDPIN.setVisibility(View.GONE);
                }
            }
        });
        //-.does farmer keep layers.
        binding.farmVisitBeforeFinance.buttonKeepLayersYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    binding.farmVisitBeforeFinance.linearUploadIDPinCert.setVisibility(View.GONE);
                }
            }
        });
        binding.farmVisitBeforeFinance.buttonKeepLayersNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    binding.farmVisitBeforeFinance.linearUploadIDPinCert.setVisibility(View.GONE);
                }
            }
        });
        //-.does farmer have buyers.
        binding.farmVisitBeforeFinance.buttonHaveBuyerYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    binding.farmVisitBeforeFinance.radioGrpHasProofOfHavingBuyers.setVisibility(View.VISIBLE);
                }
            }
        });
        binding.farmVisitBeforeFinance.buttonHaveBuyerNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    binding.farmVisitBeforeFinance.radioGrpHasProofOfHavingBuyers.setVisibility(View.GONE);
                }
            }
        });
        //-.provide proof that farmer has buyers.
        binding.farmVisitBeforeFinance.buttonProofOfHavingBuyersYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    binding.farmVisitBeforeFinance.textBuyersProofMessage.setVisibility(View.VISIBLE);
                    binding.farmVisitBeforeFinance.buttonUploadProofOfBuyer.setVisibility(View.VISIBLE);
                }
            }
        });
        binding.farmVisitBeforeFinance.buttonProofOfHavingBuyersNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    binding.farmVisitBeforeFinance.textBuyersProofMessage.setVisibility(View.GONE);
                    binding.farmVisitBeforeFinance.buttonUploadProofOfBuyer.setVisibility(View.GONE);
                }
            }
        });
    }
    private void radioButtonManagementVisitTwo(){
        //-.visit 2.
        binding.farmVisitAfterFinance.radioButtonPhoneType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        binding.farmVisitAfterFinance.radioButtonPhoneOther.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        binding.farmVisitAfterFinance.radioButtonChickenGainWeightYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        binding.farmVisitAfterFinance.radioButtonChickenGainWeightNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
    }
    private void radioButtonManagementVisitThree(){
        binding.farmVisitAfterFinance.radioButtonChickenGainWeightYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        binding.farmVisitAfterFinance.radioButtonChickenGainWeightNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
    }
    private void checkBoxManagementVisitOne(){
        binding.farmVisitBeforeFinance.checkCows.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(binding.farmVisitBeforeFinance.textAnimalKeptOnFarm.getText().toString().isEmpty()) {
                        binding.farmVisitBeforeFinance.textAnimalKeptOnFarm.setText(binding.farmVisitBeforeFinance.checkCows.getText().toString());
                    }else{
                        if (!binding.farmVisitBeforeFinance.textAnimalKeptOnFarm.getText().toString().contains(binding.farmVisitBeforeFinance.checkCows.getText().toString())) {
                            String input = binding.farmVisitBeforeFinance.textAnimalKeptOnFarm.getText().toString() + "," + binding.farmVisitBeforeFinance.checkCows.getText().toString();
                            binding.farmVisitBeforeFinance.textAnimalKeptOnFarm.setText(input);
                        }
                    }
                }else{
                    if(binding.farmVisitBeforeFinance.textAnimalKeptOnFarm.getText().toString().contains(binding.farmVisitBeforeFinance.checkCows.getText().toString())){
                        binding.farmVisitBeforeFinance.textAnimalKeptOnFarm.setText(binding.farmVisitBeforeFinance.textAnimalKeptOnFarm.getText().toString().replace(binding.farmVisitBeforeFinance.checkCows.getText().toString(),"").replace(","+binding.farmVisitBeforeFinance.checkCows.getText().toString(),""));
                    }
                }
            }
        });
        binding.farmVisitBeforeFinance.checkGoats.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(binding.farmVisitBeforeFinance.textAnimalKeptOnFarm.getText().toString().isEmpty()) {
                        binding.farmVisitBeforeFinance.textAnimalKeptOnFarm.setText(binding.farmVisitBeforeFinance.checkGoats.getText().toString());
                    }else{
                        if (!binding.farmVisitBeforeFinance.textAnimalKeptOnFarm.getText().toString().contains(binding.farmVisitBeforeFinance.checkGoats.getText().toString())) {
                            String input = binding.farmVisitBeforeFinance.textAnimalKeptOnFarm.getText().toString() + "," + binding.farmVisitBeforeFinance.checkGoats.getText().toString();
                            binding.farmVisitBeforeFinance.textAnimalKeptOnFarm.setText(input);
                        }
                    }
                }else{
                    if(binding.farmVisitBeforeFinance.textAnimalKeptOnFarm.getText().toString().contains(binding.farmVisitBeforeFinance.checkGoats.getText().toString())){
                        binding.farmVisitBeforeFinance.textAnimalKeptOnFarm.setText(binding.farmVisitBeforeFinance.textAnimalKeptOnFarm.getText().toString().replace(binding.farmVisitBeforeFinance.checkGoats.getText().toString(),"").replace(","+binding.farmVisitBeforeFinance.checkGoats.getText().toString(),""));
                    }
                }
            }
        });
        binding.farmVisitBeforeFinance.checkPigs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(binding.farmVisitBeforeFinance.textAnimalKeptOnFarm.getText().toString().isEmpty()) {
                        binding.farmVisitBeforeFinance.textAnimalKeptOnFarm.setText(binding.farmVisitBeforeFinance.checkPigs.getText().toString());
                    }else{
                        if (!binding.farmVisitBeforeFinance.textAnimalKeptOnFarm.getText().toString().contains(binding.farmVisitBeforeFinance.checkPigs.getText().toString())) {
                            String input = binding.farmVisitBeforeFinance.textAnimalKeptOnFarm.getText().toString() + "," + binding.farmVisitBeforeFinance.checkPigs.getText().toString();
                            binding.farmVisitBeforeFinance.textAnimalKeptOnFarm.setText(input);
                        }
                    }
                }else{
                    if(binding.farmVisitBeforeFinance.textAnimalKeptOnFarm.getText().toString().contains(binding.farmVisitBeforeFinance.checkPigs.getText().toString())){
                        binding.farmVisitBeforeFinance.textAnimalKeptOnFarm.setText(binding.farmVisitBeforeFinance.textAnimalKeptOnFarm.getText().toString().replace(binding.farmVisitBeforeFinance.checkPigs.getText().toString(),"").replace(","+binding.farmVisitBeforeFinance.checkPigs.getText().toString(),""));
                    }
                }
            }
        });
        binding.farmVisitBeforeFinance.checkKienyejiChicken.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(binding.farmVisitBeforeFinance.textAnimalKeptOnFarm.getText().toString().isEmpty()) {
                        binding.farmVisitBeforeFinance.textAnimalKeptOnFarm.setText(binding.farmVisitBeforeFinance.checkKienyejiChicken.getText().toString());
                    }else{
                        if (!binding.farmVisitBeforeFinance.textAnimalKeptOnFarm.getText().toString().contains(binding.farmVisitBeforeFinance.checkKienyejiChicken.getText().toString())) {
                            String input = binding.farmVisitBeforeFinance.textAnimalKeptOnFarm.getText().toString() + "," + binding.farmVisitBeforeFinance.checkKienyejiChicken.getText().toString();
                            binding.farmVisitBeforeFinance.textAnimalKeptOnFarm.setText(input);
                        }
                    }
                }else{
                    if(binding.farmVisitBeforeFinance.textAnimalKeptOnFarm.getText().toString().contains(binding.farmVisitBeforeFinance.checkKienyejiChicken.getText().toString())){
                        binding.farmVisitBeforeFinance.textAnimalKeptOnFarm.setText(binding.farmVisitBeforeFinance.textAnimalKeptOnFarm.getText().toString().replace(binding.farmVisitBeforeFinance.checkKienyejiChicken.getText().toString(),"").replace(","+binding.farmVisitBeforeFinance.checkKienyejiChicken.getText().toString(),""));
                    }
                }
            }
        });
        binding.farmVisitBeforeFinance.checkOther.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    binding.farmVisitBeforeFinance.textOtherAnimalKept.setVisibility(View.VISIBLE);
                }else{
                    binding.farmVisitBeforeFinance.textOtherAnimalKept.setVisibility(View.GONE);
                }
            }
        });
    }
    private void initCalendarVisitOne() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        //-.visit 1.
        binding.farmVisitBeforeFinance.buttonDateOfVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(FarmVisitsActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        binding.farmVisitBeforeFinance.editDateOfVisit.setText(dayOfMonth+"-"+(month+1)+"-"+year);
                    }
                },year,month,day);
                dialog.show();
            }
        });
        binding.farmVisitBeforeFinance.editDateOfVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(FarmVisitsActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        binding.farmVisitBeforeFinance.editDateOfVisit.setText(dayOfMonth+"-"+(month+1)+"-"+year);
                    }
                },year,month,day);
                dialog.show();
            }
        });
        binding.farmVisitBeforeFinance.buttonDateWhenFarmerStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(FarmVisitsActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        binding.farmVisitBeforeFinance.editDateFarmingCanStart.setText(dayOfMonth+"-"+(month+1)+"-"+year);
                    }
                },year,month,day);
                dialog.show();
            }
        });
        binding.farmVisitBeforeFinance.editDateFarmingCanStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(FarmVisitsActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        binding.farmVisitBeforeFinance.editDateFarmingCanStart.setText(dayOfMonth+"-"+(month+1)+"-"+year);
                    }
                },year,month,day);
                dialog.show();
            }
        });

    }
    private void initCalendarVisitTwo(){
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        //-.visit 2.
        binding.farmVisitAfterFinance.buttonDateOfNextVisit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                DatePickerDialog dialog = new DatePickerDialog(FarmVisitsActivity.this,new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        binding.farmVisitAfterFinance.editDateOfNextVisit.setText(dayOfMonth+"-"+(month+1)+"-"+year);
                    }
                },year,month,day);
                dialog.show();
            }
        });
        binding.farmVisitAfterFinance.editDateOfNextVisit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                DatePickerDialog dialog = new DatePickerDialog(FarmVisitsActivity.this, new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        binding.farmVisitAfterFinance.editDateOfNextVisit.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
                    }
                }, year, month, day);
                dialog.show();
            }
        });
    }
    private void initCalendarVisitThree(){
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        //-.visit 2.
        binding.farmReturnVisitAfterFinance.buttonDateOfNextVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(FarmVisitsActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        binding.farmReturnVisitAfterFinance.editDateOfNextVisit.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
                    }
                }, year, month, day);
                dialog.show();
            }
        });
        binding.farmReturnVisitAfterFinance.editDateOfNextVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(FarmVisitsActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        binding.farmReturnVisitAfterFinance.editDateOfNextVisit.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
                    }
                }, year, month, day);
                dialog.show();
            }
        });
    }
    private void initSpinnerVisitOne(Resources res){
        String[] farmVisitTypeData = res.getStringArray(R.array.type_farm_visit);
        ArrayList<String> farmVisitTypeList = new ArrayList<>(Arrays.asList(farmVisitTypeData));
        CustomAdapter adapter = new CustomAdapter(this,farmVisitTypeList);
        binding.spinFarmVisitType.setAdapter(adapter);
        binding.spinFarmVisitType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(farmVisitTypeList.get(position).toString().trim().equalsIgnoreCase("Farm visit before financing")){
                    binding.layoutFarmVisitBeforeFinance.setVisibility(View.VISIBLE);
                    binding.layoutFarmVisitAfterFinance.setVisibility(View.GONE);
                    binding.layoutFarmReturnVisitAfterFinance.setVisibility(View.GONE);
                }else if(farmVisitTypeList.get(position).toString().trim().equalsIgnoreCase("Farm visit after financing")){
                    binding.layoutFarmVisitBeforeFinance.setVisibility(View.GONE);
                    binding.layoutFarmVisitAfterFinance.setVisibility(View.VISIBLE);
                    binding.layoutFarmReturnVisitAfterFinance.setVisibility(View.GONE);
                }else if(farmVisitTypeList.get(position).toString().trim().equalsIgnoreCase("Return visit after financing")){
                    binding.layoutFarmVisitBeforeFinance.setVisibility(View.GONE);
                    binding.layoutFarmVisitAfterFinance.setVisibility(View.GONE);
                    binding.layoutFarmReturnVisitAfterFinance.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
    @Override
    public boolean onSupportNavigateUp(){
        //Intent intent = new Intent(ModifyProfileActivity.this, ProfileActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        //startActivity(intent);
        finish();
        return true;
    }
}