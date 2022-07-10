package com.example.competition1.information;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.competition1.API.DetailAPITask;
import com.example.competition1.R;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class InformationViewActivity extends AppCompatActivity {
    String insectKey;
    String sickKey;
    String imageURL;
    String name;
    Node element;
    int type;
    NodeList List;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        Intent intent=getIntent();
        insectKey=intent.getStringExtra("insectKey");
        sickKey=intent.getStringExtra("sickKey");
        type=intent.getIntExtra("type",0);
        imageURL=intent.getStringExtra("image");
        name=intent.getStringExtra("name");
        ImageView detailImage=(ImageView) findViewById(R.id.detailImage);
        //detailImage.setScaleType(ImageView.ScaleType.FIT_XY);
        //detailImage.setClipToOutline(true);
        Glide.with(this).load(imageURL).into(detailImage);
        TextView detailText=(TextView) findViewById(R.id.detailNameText);
        TextView nameEdit=(TextView) findViewById(R.id.detailNameEditText);
        TextView characterText=(TextView) findViewById(R.id.characterText);
        TextView damageText=(TextView) findViewById(R.id.damageText);
        TextView characterEdit=(TextView)findViewById(R.id.characterTextEditText);
        TextView damageEdit=(TextView)findViewById(R.id.damageEditText);
        TextView preventText=(TextView)findViewById(R.id.preventEditText);
        nameEdit.setText(name);
        if(type==1){//질병정보
            characterText.setText("○ 발생조건");
            damageText.setText("○ 증상");
            DetailAPITask task=new DetailAPITask();
            try{
                List=task.execute(sickKey,"symptom").get();
            }
            catch (Exception e){

            }
            element=List.item(0);
            characterEdit.setText(getSymptomRoute((Element) element));
            damageEdit.setText(getSymptom((Element)element));
            preventText.setText(getPrevention((Element) element,"preventionMethod"));
        }
        else if(type==2){
            detailText.setText("○ 해충명");
            damageText.setText("○ 증상");
            DetailAPITask task=new DetailAPITask();
            try{
                List=task.execute(insectKey,"pest").get();
            }
            catch (Exception e){

            }
            element=List.item(0);
            characterEdit.setText(getPestCharacter((Element) element));
            damageEdit.setText(getPestDamage((Element)element));
            preventText.setText(getPrevention((Element) element,"preventMethod"));
        }

    }
    private String getSymptomRoute(Element element){
        NodeList nodeList;
        Node node;
        String symptom="";
        try{
            nodeList=element.getElementsByTagName("developmentCondition").item(0).getChildNodes();
            node=(Node) nodeList.item(0);
            symptom+=node.getNodeValue()+"\n";}
        catch (Exception e){

        }
        return symptom.replace("<br/>","").replace("<br>","");
    }
    private String getSymptom(Element element){
        NodeList nodeList;
        Node node;
        String symptom="";
        try{
            nodeList=element.getElementsByTagName("symptoms").item(0).getChildNodes();
            node=(Node) nodeList.item(0);
            symptom+=node.getNodeValue()+"\n";}
        catch (Exception e){

        }
        return symptom.replace("<br/>","").replace("<br>","");
    }
    private String getPrevention(Element element,String param){
        NodeList nodeList;
        Node node;
        String prevent="";
        try{
            nodeList=element.getElementsByTagName(param).item(0).getChildNodes();
            node=(Node) nodeList.item(0);
            prevent+=node.getNodeValue()+"\n";}
        catch (Exception e){

        }try{
            nodeList=element.getElementsByTagName("biologyPrvnbeMth").item(0).getChildNodes();
            node=(Node) nodeList.item(0);
            prevent+=node.getNodeValue()+"\n";}
        catch (Exception e){

        }try{
            nodeList=element.getElementsByTagName("chemicalPrvnbeMth").item(0).getChildNodes();
            node=(Node) nodeList.item(0);
            prevent+=node.getNodeValue()+"\n";}
        catch (Exception e){

        }
        return prevent.replace("<br/>","").replace("<br>","");
    }
    private String getPestCharacter(Element element){
        NodeList nodeList;
        Node node;
        String pest="";
        try{
            nodeList=element.getElementsByTagName("stleInfo").item(0).getChildNodes();
            node=(Node) nodeList.item(0);
            pest+=node.getNodeValue()+"\n";}
        catch (Exception e){

        }
        return pest.replace("<br/>","").replace("<br>","");
    }
    private String getPestDamage(Element element){
        NodeList nodeList;
        Node node;
        String pest="";
        try{
            nodeList=element.getElementsByTagName("damageInfo").item(0).getChildNodes();
            node=(Node) nodeList.item(0);
            pest+=node.getNodeValue()+"\n";}
        catch (Exception e){

        }
        return pest.replace("<br/>","").replace("<br>","");
    }
}
