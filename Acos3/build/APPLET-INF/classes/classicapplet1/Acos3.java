package classicapplet1;
import javacard.framework.*;
import javacard.framework.APDU; 
import javacard.framework.Applet; 
import javacard.framework.ISOException; 
import javacard.framework.ISO7816;
import javacard.framework.Util;
import java.io.IOException;
import java.lang.*;

/**
 *
 * @author ABDERRAHIM&ZINEB
 */

public class Acos3 extends Applet {
    
    public static final byte ma_classe=(byte) 0x80;
    public static final boolean selectApp = true;
    public static byte[]   memorry =new byte[1000];

    
    int offset =0;
    // instructions
    public static final byte ins_lecture = 0x00;
    public static final byte insSubmit = (byte) 0x02;
    public static final byte insChangeCode = (byte) 0x03;
    public static final byte insClear = (byte) 0x04;
    public static final byte insRead = (byte) 0xb2;
    public static final byte insWrite = (byte) 0xd0;
    public static final byte inscreate =(byte) 0xd1;
    public static final byte insSelect =(byte) 0xa4;
    
    private static final byte NbreEssaiMax = 0x03;
    
    private OwnerPIN codeIc,codePin;
  
    
    public static byte   initialIc[]= new byte[4];
    public static  byte   initialIc2[];
    public static byte   initialPin[]= new byte[4];
    
    private byte verif=4;  
    
    
    public static  byte   FF02= (byte) 0x03;
    public static  byte [] selectedFile = {(byte)0xFF,(byte)0x02,(byte)0x00,(byte)0x00};// the third attribute is for the index of file in the FF04 and the 4 is for index in memoiry
    public static  byte [] selectedFileI = {(byte)0xFF,(byte)0x02,(byte)0x00,(byte)0x00};
    public static final byte [] FF03= {(byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01,(byte) 0x00,(byte)  0x00, (byte) 0x00, (byte) 0x00};
    public static final byte [] FF03I= {(byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01,(byte) 0x00,(byte)  0x00, (byte) 0x00, (byte) 0x00};

    public static byte [] FF04 =       {(byte) 0x01, (byte) 0x01, (byte) 0x80, (byte) 0x80,(byte) 0xFF,(byte)  0x02,//pour FF02
                                        (byte) 0x08, (byte) 0x01, (byte) 0x80, (byte) 0x40,(byte) 0xFF,(byte)  0x03,//pour FF03
                                        (byte) 0x06, (byte) 0x30, (byte) 0x80, (byte) 0x40,(byte) 0xFF,(byte)  0x04,// pour FF04
                                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,(byte) 0x00,(byte)  0x00,/**/
                                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,(byte) 0x00,(byte)  0x00,/**/
                                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,(byte) 0x00,(byte)  0x00,/**/
                                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,(byte) 0x00,(byte)  0x00,/**/
                                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,(byte) 0x00,(byte)  0x00,/**/
                                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,(byte) 0x00,(byte)  0x00,/**/
                                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,(byte) 0x00,(byte)  0x00,/**/
                                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,(byte) 0x00,(byte)  0x00,/**/
                                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,(byte) 0x00,(byte)  0x00,/**/
                                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,(byte) 0x00,(byte)  0x00,/**/
                                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,(byte) 0x00,(byte)  0x00,/**/
                                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,(byte) 0x00,(byte)  0x00,/**/
                                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,(byte) 0x00,(byte)  0x00,/**/
                                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,(byte) 0x00,(byte)  0x00,/**/
                                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,(byte) 0x00,(byte)  0x00,/**/
                                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,(byte) 0x00,(byte)  0x00,/**/
                                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,(byte) 0x00,(byte)  0x00,/**/
                                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,(byte) 0x00,(byte)  0x00,/**/
                                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,(byte) 0x00,(byte)  0x00,/**/
                                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,(byte) 0x00,(byte)  0x00,/**/
                                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,(byte) 0x00,(byte)  0x00,/**/
                                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,(byte) 0x00,(byte)  0x00,/**/
                                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,(byte) 0x00,(byte)  0x00,/**/
                                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,(byte) 0x00,(byte)  0x00,/**/
                                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,(byte) 0x00,(byte)  0x00,/**/
                                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,(byte) 0x00,(byte)  0x00,/**/
                                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,(byte) 0x00,(byte)  0x00,/**/
                                        };
    public static byte [] FF04I =      {(byte) 0x01, (byte) 0x01, (byte) 0x80, (byte) 0x80,(byte) 0xFF,(byte)  0x02,//pour FF02
                                        (byte) 0x08, (byte) 0x01, (byte) 0x80, (byte) 0x40,(byte) 0xFF,(byte)  0x03,//pour FF03
                                        (byte) 0x06, (byte) 0x30, (byte) 0x80, (byte) 0x40,(byte) 0xFF,(byte)  0x04,// pour FF04
                                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,(byte) 0x00,(byte)  0x00,/**/
                                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,(byte) 0x00,(byte)  0x00,/**/
                                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,(byte) 0x00,(byte)  0x00,/**/
                                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,(byte) 0x00,(byte)  0x00,/**/
                                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,(byte) 0x00,(byte)  0x00,/**/
                                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,(byte) 0x00,(byte)  0x00,/**/
                                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,(byte) 0x00,(byte)  0x00,/**/
                                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,(byte) 0x00,(byte)  0x00,/**/
                                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,(byte) 0x00,(byte)  0x00,/**/
                                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,(byte) 0x00,(byte)  0x00,/**/
                                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,(byte) 0x00,(byte)  0x00,/**/
                                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,(byte) 0x00,(byte)  0x00,/**/
                                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,(byte) 0x00,(byte)  0x00,/**/
                                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,(byte) 0x00,(byte)  0x00,/**/
                                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,(byte) 0x00,(byte)  0x00,/**/
                                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,(byte) 0x00,(byte)  0x00,/**/
                                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,(byte) 0x00,(byte)  0x00,/**/
                                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,(byte) 0x00,(byte)  0x00,/**/
                                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,(byte) 0x00,(byte)  0x00,/**/
                                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,(byte) 0x00,(byte)  0x00,/**/
                                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,(byte) 0x00,(byte)  0x00,/**/
                                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,(byte) 0x00,(byte)  0x00,/**/
                                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,(byte) 0x00,(byte)  0x00,/**/
                                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,(byte) 0x00,(byte)  0x00,/**/
                                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,(byte) 0x00,(byte)  0x00,/**/
                                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,(byte) 0x00,(byte)  0x00,/**/
                                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,(byte) 0x00,(byte)  0x00,/**/
                                        };

   public static void install(byte[] bArray, short bOffset, byte bLength) {
        new Acos3();
    }

    protected Acos3() {
        for(byte i=0 ; i<4 ; i++){
            initialIc[i] =FF03[i];
            initialPin[i] =FF03[i+4];
        }
        codeIc = new OwnerPIN(NbreEssaiMax, (byte)0x04);
        codeIc.update(initialIc, (short) 0,(byte)0x04);
        codePin = new OwnerPIN(NbreEssaiMax, (byte)0x04);
        codePin.update(initialPin, (short) 0,(byte)0x04);//0x04 is the lenght of the code
        register();
    }
    //SelectFile 
    public boolean selectF(APDU apdu1) {
        byte[] buf = apdu1.getBuffer();
        int k=0;
        for(byte i =0 ; i < (byte)FF02*6; i+=6){ 
            if(i>=18)k+=(int)FF04[i]*(int)FF04[i+1];
            if(buf[5]==(byte)FF04[i+4] & buf[6]==(byte)FF04[i+5]){
                selectedFile[0]= FF04[i+4];// id MSB
                selectedFile[1]= FF04[i+5];// id LSB
                selectedFile[2]= (byte)i;//position in FF04
                selectedFile[3]=(byte)k;// position max in memory
                return true;
            }
        }
        ISOException.throwIt(ISO7816.SW_APPLET_SELECT_FAILED);
        return false;
    }

    //ClearCard
    public void ClearCard(APDU apdu1) {
        if(!codeIc.isValidated() | !codePin.isValidated()){
            ISOException.throwIt(ISO7816.SW_SECURITY_STATUS_NOT_SATISFIED);
            return;
        }
        FF02=0x03;
        for(byte i = 0; i<FF03.length;i++)FF03[i]=FF03I[i];
        for(short i = 0; i<(short)FF04.length;i++)FF04[i]=FF04I[i];
        for(byte i = (byte)0; i<(byte)4;i++)selectedFile[i]=selectedFileI[i];
        memorry =new byte[1000];
        codeIc.update(initialIc, (short) 0,(byte)0x04);
        codePin.update(initialPin, (short) 0,(byte)0x04);
        return;
    }
    //submitCode
    public void SubmitF(APDU apdu1) {
        byte[] buf = apdu1.getBuffer();
        if(buf[ISO7816.OFFSET_P1] == 0x07){// p1==0x07 pour codeIc
            if (buf[ISO7816.OFFSET_LC] != 4)ISOException.throwIt(ISO7816.SW_WRONG_DATA);
            if(!codeIc.check(buf,(short) ISO7816.OFFSET_CDATA,(byte)4))ISOException.throwIt(ISO7816.SW_SECURITY_STATUS_NOT_SATISFIED);
        }
        else if (buf[ISO7816.OFFSET_P1] == 0x06){// p1==0x06 pour codePin
            if (buf[ISO7816.OFFSET_LC] != 4)ISOException.throwIt(ISO7816.SW_WRONG_DATA);
            if(!codePin.check(buf,(short) ISO7816.OFFSET_CDATA,(byte)4))ISOException.throwIt(ISO7816.SW_SECURITY_STATUS_NOT_SATISFIED);
        }
        return;        
    }
    
    // to change code
    public void ChangeCode(APDU apdu1) {
        if(!codeIc.isValidated() | !codePin.isValidated())ISOException.throwIt(ISO7816.SW_SECURITY_STATUS_NOT_SATISFIED);
        else
        {
        byte[] buf = apdu1.getBuffer();
        if(buf[ISO7816.OFFSET_P1] == 0x07){// p1==0x07 pour codeIc
            if (buf[ISO7816.OFFSET_LC] != 4)ISOException.throwIt(ISO7816.SW_WRONG_DATA);
            else{
                codeIc.update(buf, (short) buf[ISO7816.OFFSET_CDATA] ,(byte)0x04);
            };
        }
        else if (buf[ISO7816.OFFSET_P1] == 0x06){// p1==0x06 pour codePin
            if (buf[ISO7816.OFFSET_LC] != 4)ISOException.throwIt(ISO7816.SW_WRONG_DATA);
            if(!codePin.check(buf,(short) ISO7816.OFFSET_CDATA,(byte)4))ISOException.throwIt(ISO7816.SW_SECURITY_STATUS_NOT_SATISFIED);
            else{
                codePin.update(buf, (short) buf[ISO7816.OFFSET_CDATA],(byte)0x04);
            };
        } 
    }
        return;
    }
    
    //ReadFiles
    public void ReadFile(APDU apdu1) {
        byte[] buf = apdu1.getBuffer();
        if((FF04[(byte)(selectedFile[2]+2)]==(byte)0x80)){//if the security of read is on codeIc    
            //apdu1.setOutgoingAndSend((short)0,(short)10);
            if( !codeIc.isValidated()){
                ISOException.throwIt(ISO7816.SW_SECURITY_STATUS_NOT_SATISFIED);
                return;
            }
        }
        if((FF04[(byte)(selectedFile[2]+2)]==(byte)0x40)){//if the security of read is on codeIc    
            //apdu1.setOutgoingAndSend((short)0,(short)5);
            if( !codePin.isValidated()){
                ISOException.throwIt(ISO7816.SW_SECURITY_STATUS_NOT_SATISFIED);
                return;
            }
        }
        short offsetLow = (short)buf[ISO7816.OFFSET_P1];
        short offsetHigh = (short)buf[ISO7816.OFFSET_P2];
        if(selectedFile[0]==(byte)0xFF){
            if(selectedFile[1]==(byte)0x02){
                buf[0] = FF02;
                if(offsetHigh>1)offsetHigh=1;
                apdu1.setOutgoingAndSend((short)offsetLow,(short)offsetHigh);
                return;
            }
            if(selectedFile[1]==(byte)0x03){
                 for(int i = 0 ; i<FF03.length; i++){
                     buf[(short) i]=FF03[(short)i];
                }
                if(offsetHigh>(short)FF03.length)offsetHigh=(short)FF03.length;
                apdu1.setOutgoingAndSend((short)offsetLow,(short)offsetHigh);
                return;
            }
            if(selectedFile[1]==(byte)0x04){
                 for(int i = 0 ; i<100; i++){
                     buf[(short) i]=FF04[(short)i];
                }
                if(offsetHigh>(short)FF04.length)offsetHigh=(short)FF04.length;
                apdu1.setOutgoingAndSend((short)offsetLow,(short)offsetHigh);
                return;
            }
        }
        for(byte i=(byte)(selectedFile[3]-FF04[(byte)selectedFile[2]]*FF04[(byte)selectedFile[2]+1]);i< (byte)(selectedFile[3] +1) ;i++ ){
            buf[(byte)(i-(byte)(selectedFile[3]-FF04[(byte)selectedFile[2]]*FF04[(byte)selectedFile[2]+1]))]=memorry[(byte)i];
        }
        if(offsetHigh>(short)(FF04[(byte)selectedFile[2]]*FF04[(byte)selectedFile[2]+1]))offsetHigh=(short)(FF04[(byte)selectedFile[2]]*FF04[(byte)selectedFile[2]+1]);
        apdu1.setOutgoingAndSend((short)offsetLow,(short)offsetHigh);
        return;
        
        
        
        
    }
    //cla ins p1 p2 lc daaataa so p1&p2 <= id of file lc <= langueure
    //first search the position
    //WriteFile
    public void WriteFile(APDU apdu1) {
        byte[] buf = apdu1.getBuffer();
        if( buf[ISO7816.OFFSET_P1]== (byte)0x01){//p1 = 1 --> Write in FF04 so Apdu is in 6 bytes
            if(!(buf[ISO7816.OFFSET_LC]== (byte)0x06)){//verify the len of data should be ==6
                ISOException.throwIt(ISO7816.SW_WRONG_DATA);
                return;
            }
            if((FF04[(byte)(0x0f)]==(byte)0x80)){
                if( !codeIc.isValidated()){
                    ISOException.throwIt(ISO7816.SW_SECURITY_STATUS_NOT_SATISFIED);
                    return;
                }
            }
            if((FF04[(byte)(0x0f)]==(byte)0x40)){
                if( !codePin.isValidated()){
                ISOException.throwIt(ISO7816.SW_SECURITY_STATUS_NOT_SATISFIED);
                return;
                }
            }      
            if(! existFileFF04(apdu1)){// if the file NOT alderly exist in the FF04
                for(byte i=0 ; i<6; i++){//ADD IT ! existFileFF04(apdu1)IN the last place in FF04 index of record is FF02
                    FF04[(short)(FF02*6 + i)]= buf[(short)i+5];//mbprb
                }
                FF02 ++;
                return;
            }
            // if alderly exist search for it and update them!!
            for(byte j =0 ; j < (byte)FF02*6; j+=6){
                if(buf[9]==(byte)FF04[j+4] & buf[10]==(byte)FF04[j+5]){
                    for(byte i=0 ; i<6; i++){
                        FF04[j + i]= buf[i+5];
                    }
                    FF02+=1;
                    return;
                }
            }
            return;
        }
        if( buf[ISO7816.OFFSET_P1]== (byte)0x02){
            byte dataLenth=(byte)(FF04[(byte)(selectedFile[2])]*FF04[(byte)(selectedFile[2]+1)]);
            if(!(buf[ISO7816.OFFSET_LC]== dataLenth)){
                ISOException.throwIt(ISO7816.SW_WRONG_DATA);
                return;
            }
            if((FF04[(byte)(selectedFile[2]+3)]==(byte)0x80)){
                if( !codeIc.isValidated()){
                    ISOException.throwIt(ISO7816.SW_SECURITY_STATUS_NOT_SATISFIED);
                    return;
                }
            }
            if((FF04[(byte)(selectedFile[2]+3)]==(byte)0x40)){
                if( !codePin.isValidated()){
                    ISOException.throwIt(ISO7816.SW_SECURITY_STATUS_NOT_SATISFIED);
                    return;
                }
            }
            //if all tests are good so add the data to memory
            for(byte i=(byte)((byte)selectedFile[3]-buf[ISO7816.OFFSET_LC]);i< (byte)(selectedFile[3] +1) ;i++ ){
                memorry[(byte)i]=buf[(byte)(i+5-(byte)((byte)selectedFile[3]-buf[ISO7816.OFFSET_LC]))];
            }
            
            return;
        }
    }
    //File exist
    public boolean existFileFF04(APDU apdu1) {
        byte[] buf = apdu1.getBuffer();
        for(byte i =0 ; i < (byte)FF02*6; i+=6){
            if(buf[9]==(byte)FF04[i+4] & buf[10]==(byte)FF04[i+5]){
               return true; 
            }
        }
        return false;
    }    
    // process
    public void process(APDU apdu) {
        if (selectingApplet()) return;        
        byte[] buf = apdu.getBuffer();
        if (buf[ISO7816.OFFSET_CLA]!=ma_classe)
        
            ISOException.throwIt(ISO7816.SW_CLA_NOT_SUPPORTED);
        switch(buf[ISO7816.OFFSET_INS]){
            case insSelect :
                selectF(apdu);
                return;
            case insSubmit :
                SubmitF(apdu);
                return;
            case insChangeCode :
                ChangeCode(apdu);
                return;
            case insClear :
                ClearCard(apdu);
                return;
            case insRead :
                ReadFile(apdu);
                return;
            case insWrite :
                WriteFile(apdu);
                return;
            default :
                ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
        }
    }
    
}
