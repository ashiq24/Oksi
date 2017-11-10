package com.buet.sadiq.oksi.Adapter;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.buet.sadiq.oksi.R;
import com.buet.sadiq.oksi.helper.ImageHelper;
import com.buet.sadiq.oksi.viewHolder.FaceViewHolder;
import com.microsoft.projectoxford.face.contract.Face;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FaceListAdapter extends RecyclerView.Adapter<FaceViewHolder>{
    List<Face> faces;
    List<Bitmap> faceThumbnails;
    Context context;

    public FaceListAdapter(Face[] detectionResult,Bitmap mBitmap,Context context) {

        this.context=context;
        faces = new ArrayList<>();
        faceThumbnails = new ArrayList<>();

         if (detectionResult != null) {
             faces = Arrays.asList(detectionResult);
             for (Face face : faces) {
                 try {
                     // Crop face thumbnail with five main landmarks drawn from original image.
                     faceThumbnails.add(ImageHelper.generateFaceThumbnail(
                             mBitmap, face.faceRectangle));
                    } catch (IOException e) {
                        // Show the exception when generating face thumbnail fails.

                    }
                }
            }
        }

      /*  @Override
        public boolean isEnabled(int position) {
            return false;
        }

        @Override
        public int getCount() {
            return faces.size();
        }

        @Override
        public Object getItem(int position) {
            return faces.get(position);
        }*/


    @Override
    public FaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_face, parent,
                false);
        return new FaceViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FaceViewHolder holder, int position) {

        holder.bindToFace(faces.get(position),faceThumbnails.get(position),position+1);
    }

    @Override
        public long getItemId(int position) {
            return position;
        }

    @Override
    public int getItemCount() {
        return faces.size();
    }


    /*@Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater layoutInflater =
                        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.item_face_with_description, parent,
                        false);
            }
            convertView.setId(position);

            // Show the face thumbnail.
            ((ImageView) convertView.findViewById(R.id.face_thumbnail)).setImageBitmap(
                    faceThumbnails.get(position));

            // Show the face details.
            DecimalFormat formatter = new DecimalFormat("#0.0");
            String face_description = String.format("Age: %s  Gender: %s\nHair: %s " +
                            "\n%s\n%s" + "\nSmile: %s"+
                            "\n%s \nGlassesType: %s\nAccessories: %s",

                    faces.get(position).faceAttributes.age,
                    faces.get(position).faceAttributes.gender,
                    getHair(faces.get(position).faceAttributes.hair),
                    getFacialHair(faces.get(position).faceAttributes.facialHair),
                    getMakeup((faces.get(position)).faceAttributes.makeup),
                    String.valueOf(faces.get(position).faceAttributes.smile*100),
                    getEmotion(faces.get(position).faceAttributes.emotion),
                   // faces.get(position).faceAttributes.occlusion.foreheadOccluded,
                    //faces.get(position).faceAttributes.blur.blurLevel,
                    //faces.get(position).faceAttributes.occlusion.eyeOccluded,
                    //faces.get(position).faceAttributes.exposure.exposureLevel,
                    //faces.get(position).faceAttributes.occlusion.mouthOccluded,
                    //faces.get(position).faceAttributes.noise.noiseLevel,
                    faces.get(position).faceAttributes.glasses,
                    //getHeadPose(faces.get(position).faceAttributes.headPose),
                    getAccessories(faces.get(position).faceAttributes.accessories)
                    );
            ((TextView) convertView.findViewById(R.id.text_detected_face)).setText(face_description);

            return convertView;
        }*/

}