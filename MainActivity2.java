package com.swufe.work;

import android.content.Context;

import com.swufe.work.DiaryTable2;


//数据模型全局
class Model {
    private Context mContext;
    private static Model model=new Model();
    private DiaryTable2 diaryTable2;

    private Model(){}

    public static Model getInstance(){return model;}

    public void init(Context context){
        mContext=context;
        diaryTable2=new DiaryTable2(mContext);
    }


    public DiaryTable2 getDiaryTableDao(){
        return diaryTable2;
    }

}
