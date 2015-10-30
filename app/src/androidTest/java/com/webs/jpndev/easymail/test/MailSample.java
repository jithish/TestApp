package com.webs.jpndev.easymail.test;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;
import com.webs.jpndev.easymail.MailActivity;


public class MailSample extends ActivityInstrumentationTestCase2<MailActivity> {
  	private Solo solo;
  	
  	public MailSample() {
		super(MailActivity.class);
  	}

  	public void setUp() throws Exception {
        super.setUp();
		solo = new Solo(getInstrumentation());
		getActivity();
  	}
  
   	@Override
   	public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
  	}
  
	public void testRun() {
        //Take screenshot
        solo.takeScreenshot();
        //Wait for activity: 'com.webs.jpndev.easymail.MailActivity'
		solo.waitForActivity(com.webs.jpndev.easymail.MailActivity.class, 2000);
        //Sleep for 6165 milliseconds
		solo.sleep(1000);
        //Click on Empty Text View
		solo.clickOnView(solo.getView(com.webs.jpndev.easymail.R.id.select_email_atxv));
        //Sleep for 15939 milliseconds
		solo.sleep(500);
        //Enter the text: 'Jithishpn007@gmail.com'
		solo.clearEditText((android.widget.EditText) solo.getView(com.webs.jpndev.easymail.R.id.select_email_atxv));
		solo.enterText((android.widget.EditText) solo.getView(com.webs.jpndev.easymail.R.id.select_email_atxv), "Jithishpn007@gmail.com");
        //Sleep for 1313 milliseconds
		solo.sleep(500);
        //Click on Jithishpn007@gmail.com
		solo.clickOnView(solo.getView(com.webs.jpndev.easymail.R.id.select_email_atxv));
        //Sleep for 4938 milliseconds
		solo.sleep(500);
        //Enter the text: 'jithishpn007@gmail.com'
		solo.clearEditText((android.widget.EditText) solo.getView(com.webs.jpndev.easymail.R.id.select_email_atxv));
		solo.enterText((android.widget.EditText) solo.getView(com.webs.jpndev.easymail.R.id.select_email_atxv), "jithishpn007@gmail.com");
        //Sleep for 1075 milliseconds
		solo.sleep(1075);
        //Click on Empty Text View
		solo.clickOnView(solo.getView(com.webs.jpndev.easymail.R.id.editTextMessage));
        //Sleep for 783 milliseconds
		solo.sleep(500);
        //Click on Empty Text View
		solo.clickOnView(solo.getView(com.webs.jpndev.easymail.R.id.editTextSubject));
        //Sleep for 5139 milliseconds
		solo.sleep(500);
        //Enter the text: 'testing'
		solo.clearEditText((android.widget.EditText) solo.getView(com.webs.jpndev.easymail.R.id.editTextSubject));
		solo.enterText((android.widget.EditText) solo.getView(com.webs.jpndev.easymail.R.id.editTextSubject), "testing");
        //Click on Empty Text View
		solo.clickOnView(solo.getView(com.webs.jpndev.easymail.R.id.editTextMessage));
        //Sleep for 5073 milliseconds
		solo.sleep(500);
        //Enter the text: 'android '
		solo.clearEditText((android.widget.EditText) solo.getView(com.webs.jpndev.easymail.R.id.editTextMessage));
		solo.enterText((android.widget.EditText) solo.getView(com.webs.jpndev.easymail.R.id.editTextMessage), "android");
        //Sleep for 3667 milliseconds
		solo.sleep(500);
        //Click on Browse
		solo.clickOnView(solo.getView(com.webs.jpndev.easymail.R.id.browse));
        //Wait for activity: 'com.webs.jpndev.easymail.FilePicker'
		assertTrue("com.webs.jpndev.easymail.FilePicker is not found!", solo.waitForActivity(com.webs.jpndev.easymail.FilePicker.class));
        //Sleep for 13836 milliseconds
		solo.sleep(500);
        //Scroll to 30293286
		android.widget.ListView listView0 = (android.widget.ListView) solo.getView(android.widget.ListView.class, 0);
		solo.scrollListToLine(listView0, 0);
        //Sleep for 2284 milliseconds
		solo.sleep(2284);
        //Click on android
		solo.clickOnView(solo.getView(com.webs.jpndev.easymail.R.id.editTextMessage));
        //Sleep for 6228 milliseconds
		solo.sleep(500);
        //Enter the text: 'android no attachment '
		solo.clearEditText((android.widget.EditText) solo.getView(com.webs.jpndev.easymail.R.id.editTextMessage));
		solo.enterText((android.widget.EditText) solo.getView(com.webs.jpndev.easymail.R.id.editTextMessage), "androidno attachment");
        //Sleep for 4087 milliseconds
		solo.sleep(500);
        //Click on Send
		solo.clickOnView(solo.getView(com.webs.jpndev.easymail.R.id.send_btn));
        //Sleep for 12864 milliseconds
		solo.sleep(500);
        //Click on android no attachment
		solo.clickOnView(solo.getView(com.webs.jpndev.easymail.R.id.editTextMessage));
        //Sleep for 3690 milliseconds
		solo.sleep(500);
        //Press menu back key
		solo.goBack();
	}
}
