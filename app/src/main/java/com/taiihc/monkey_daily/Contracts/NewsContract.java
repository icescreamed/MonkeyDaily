package com.taiihc.monkey_daily.Contracts;

import com.taiihc.monkey_daily.BasePresenter;
import com.taiihc.monkey_daily.BaseView;
import com.taiihc.monkey_daily.Beans.NewsEntry;
import com.taiihc.monkey_daily.Beans.TopNewsEntry;

import java.util.List;

/**
 * 代表MVP中V和P的协议
 */
public interface NewsContract {
    interface Presenter extends BasePresenter{


    }
    interface View extends BaseView<Presenter>{
        void setHeaderData(List<TopNewsEntry> entryList);
        void setItemData(List<NewsEntry> entryList);
        void loadMoreData(List<NewsEntry>entryList);
        void refreshData(List<NewsEntry> itemList,List<TopNewsEntry> headList);
        void loadDataProccess();
        void refreshDataProcess();
        void loadDatafailed();
        void refreshDatafailed();
    }
}
