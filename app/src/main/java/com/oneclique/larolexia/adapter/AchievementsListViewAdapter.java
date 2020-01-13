package com.oneclique.larolexia.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.oneclique.larolexia.LaroLexiaSQLite.LaroLexiaSQLiteModel.AchievementsModel;
import com.oneclique.larolexia.R;

import java.util.List;

public class AchievementsListViewAdapter extends BaseAdapter {

    private Context context;
    private List<AchievementsModel> achievementsModels;
    private LayoutInflater layoutInflater;

    public AchievementsListViewAdapter(Context context, List<AchievementsModel> achievementsModels){
        this.context = context;
        this.achievementsModels = achievementsModels;
        this.layoutInflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return achievementsModels.size();
    }

    @Override
    public Object getItem(int position) {
        return achievementsModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.item_achievement, null, true);

        TextView mTextViewAchievementsScore = convertView.findViewById(R.id.mTextViewAchievementsScore);
        TextView mTextViewAchievementsCategory = convertView.findViewById(R.id.mTextViewAchievementsCategory);
        TextView mTextViewAchievementsLevel = convertView.findViewById(R.id.mTextViewAchievementsLevel);
        TextView mTextViewAchievementsLetter = convertView.findViewById(R.id.mTextViewAchievementsLetter);

        ImageView[] mImageViewAchievementsStars = {
                convertView.findViewById(R.id.mImageViewAchievementsStar1),
                convertView.findViewById(R.id.mImageViewAchievementsStar2),
                convertView.findViewById(R.id.mImageViewAchievementsStar3),
        };
        mTextViewAchievementsScore.setText(achievementsModels.get(position).getA_time());
        mTextViewAchievementsCategory.setText(achievementsModels.get(position).getA_gameMode());
        mTextViewAchievementsLevel.setText(achievementsModels.get(position).getA_level());
        mTextViewAchievementsLetter.setText(achievementsModels.get(position).getA_letter());
        int stars = Integer.parseInt(achievementsModels.get(position).getA_star());

        mImageViewAchievementsStars[0].setImageResource(R.drawable.ic_starlocked);
        mImageViewAchievementsStars[1].setImageResource(R.drawable.ic_starlocked);
        mImageViewAchievementsStars[2].setImageResource(R.drawable.ic_starlocked);
        for (int i = 0; i < stars; i++){
            mImageViewAchievementsStars[i].setImageResource(R.drawable.ic_star);
        }

        return convertView;
    }
}
