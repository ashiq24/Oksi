package com.buet.sadiq.oksi.viewHolder;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.buet.sadiq.oksi.R;
import com.microsoft.projectoxford.face.contract.Accessory;
import com.microsoft.projectoxford.face.contract.Emotion;
import com.microsoft.projectoxford.face.contract.Face;
import com.microsoft.projectoxford.face.contract.FacialHair;
import com.microsoft.projectoxford.face.contract.Glasses;
import com.microsoft.projectoxford.face.contract.Hair;
import com.microsoft.projectoxford.face.contract.HeadPose;
import com.microsoft.projectoxford.face.contract.Makeup;

/**
 * Created by sadiq on 11/9/17.
 */


public class FaceViewHolder extends RecyclerView.ViewHolder{

    TextView pos;
    TextView age;
    TextView gender;
    TextView hair;
    TextView facialHairOrMakeup;
    TextView emotion;
    TextView glasses;
    TextView accessories;
    ImageView faceThum;


    public FaceViewHolder(View itemView) {
        super(itemView);

        pos=itemView.findViewById(R.id.pos);
        age=itemView.findViewById(R.id.age);
        gender=itemView.findViewById(R.id.gender);
        hair=itemView.findViewById(R.id.hair);
        facialHairOrMakeup=itemView.findViewById(R.id.facial_hair);
        emotion=itemView.findViewById(R.id.emotion);
        glasses=itemView.findViewById(R.id.glasses);
        accessories=itemView.findViewById(R.id.accessories);
        faceThum=itemView.findViewById(R.id.face_thumbnail);

    }

    public void bindToFace(Face face, Bitmap image,int id){

        String g="";
        String a[] = face.faceAttributes.gender.split(":");
        g=a[0].trim();

        if(g.equals("male")){
            gender.setText("Male");
            facialHairOrMakeup.setText(getFacialHair(face.faceAttributes.facialHair));
        }
        else {
            gender.setText("Female");
            facialHairOrMakeup.setText(getMakeup(face.faceAttributes.makeup));
        }

        pos.setText(String.valueOf(id));
        age.setText("Age: "+ String.valueOf(face.faceAttributes.age-5));
        hair.setText(getHair(face.faceAttributes.hair));
        emotion.setText(getEmotion(face.faceAttributes.emotion));
        glasses.setText(getGlasses(face.faceAttributes.glasses));
        accessories.setText(getAccessories(face.faceAttributes.accessories));
        faceThum.setImageBitmap(image);

    }





    private String getGlasses(Glasses glasses){
        return "Glasses: "+ String.valueOf(glasses);
    }




    private String getHair(Hair hair) {
        if (hair.hairColor.length == 0)
        {
            if (hair.invisible)
                return "Invisible";
            else
                return "Bald";
        }
        else
        {
            int maxConfidenceIndex = 0;
            double maxConfidence = 0.0;

            for (int i = 0; i < hair.hairColor.length; ++i)
            {
                if (hair.hairColor[i].confidence > maxConfidence)
                {
                    maxConfidence = hair.hairColor[i].confidence;
                    maxConfidenceIndex = i;
                }
            }



            return "Hair Color: "+ hair.hairColor[maxConfidenceIndex].color.toString();
        }
    }

    private String getMakeup(Makeup makeup) {
        //return  (makeup.eyeMakeup || makeup.lipMakeup) ? "Yes" : "No" ;
        String e,l;
        if(makeup.eyeMakeup){
            e="Yes";
        }
        else {
            e="No";
        }

        if(makeup.lipMakeup){
            l="Yes";
        }
        else {
            l="No";
        }
        return "Eye Makeup: "+e+
                "\nLip Makeup: " + l;
    }

    private String getAccessories(Accessory[] accessories) {
        if (accessories.length == 0)
        {
            return "NoAccessories";
        }
        else
        {
            String[] accessoriesList = new String[accessories.length];
            for (int i = 0; i < accessories.length; ++i)
            {
                accessoriesList[i] = accessories[i].type.toString();
            }

            return "Accessories: "+ TextUtils.join(",", accessoriesList);
        }
    }

    private String getFacialHair(FacialHair facialHair) {
        //return (facialHair.moustache + facialHair.beard + facialHair.sideburns > 0) ?
        // "Yes" : "No";

        return "Moustace: "+(int)(facialHair.moustache*100)+
                "\nBeard: "+(int)(facialHair.beard*100)+
                "\nSideburns: "+(int)(facialHair.sideburns*100);
    }

    private String getEmotion(Emotion emotion)
    {

        double value[] = {emotion.anger,emotion.contempt,emotion.disgust,emotion.fear,
                emotion.happiness,emotion.neutral,emotion.sadness,emotion.surprise};

        String emotionT[] = {"Anger","Contempt","Disgust","Fear","Smile","Neutral",
                "Sadness","Surprise"};

        int max1=0,max2=0;
        for (int i = 0; i < value.length; i++) {
            if(value[i]>value[max1]){
                max1=i;
            }
        }

        double val2=0;
        for (int i = 0; i < value.length; i++) {
            if(value[i]<value[max1]){
                if(value[i]>val2){
                    val2=value[i];
                    max2=i;
                }
            }
        }

        return emotionT[max1] + ": "+ (int)(value[max1]*100)+
                "\n" + emotionT[max2] + ": " + (int)(value[max2]*100);
    }

    private String getHeadPose(HeadPose headPose)
    {
        return String.format("Pitch: %s, Roll: %s, Yaw: %s", headPose.pitch, headPose.roll, headPose.yaw);
    }
}
