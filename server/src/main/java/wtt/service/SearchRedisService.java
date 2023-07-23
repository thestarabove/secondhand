package wtt.service;

import java.util.List;

public interface SearchRedisService {
    public int addSearchByUserId(Integer userId,String searchKey);
    public int addSearchHistoryByUserId(Integer userId,String searchKey);
    public int deleteSearchHistoryByUserId(Integer userId,String searchKey);
    public  List<String>  getSearchHistoryByUserId(Integer userId);
    public int incrementScoreByUserId(int userId,String searchKey);
    public List<String> getHotList(String searchKey);
    public int incrementScore(String searchKey);
    Boolean isProductInSearch(String userId, String goodName);
    boolean  handleProductInSearch(String name);
}
