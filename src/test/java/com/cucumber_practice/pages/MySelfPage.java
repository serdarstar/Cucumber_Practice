package com.cucumber_practice.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.cucumber_practice.utilities.Driver;

public class MySelfPage extends TopNavigationBar {
	public MySelfPage() {
		PageFactory.initElements(Driver.get(), this);
	}
	
	@FindBy (xpath = "(//*[@class='title is-6'])[3]")
	public WebElement teamName;

}
