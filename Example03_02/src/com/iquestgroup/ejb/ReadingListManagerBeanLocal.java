package com.iquestgroup.ejb;

import java.util.List;

import javax.ejb.Local;

@Local
public interface ReadingListManagerBeanLocal {

	void addTitle(String title);

	boolean removeTitle(String title);

	int getCount();

	List<String> getReadingList();

}
