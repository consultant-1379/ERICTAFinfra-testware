package com.ericsson.cifwk.ui.test.pages;

import com.ericsson.cifwk.taf.ui.BrowserTab;
import com.ericsson.cifwk.taf.ui.UI;
import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.*;


public class DeliveryPackageToDropViewModel extends GenericViewModel {
	
    private Link productLink;
	
	@UiComponentMapping(id="release_menu")
    private Link releaseMenu;
	
	@UiComponentMapping(id="pkgs_link")
    private Link pkgsLink;
	
    private Link pkgLink;
    
    @UiComponentMapping(id="allPkgRevs_link")
    private Link viewAllPkgRevs;
    
	private Link deliverLink;
	
	@UiComponentMapping(id="cpl_link")
    private Link cpl_link;
		
	private Link newDeliverLink;
	
	
	@UiComponentMapping(name="username")
    private TextBox usernameInput;
	
    @UiComponentMapping(name="password")
    private TextBox passwordInput;
    
    @UiComponentMapping(name="ciportal_login")
    private Button loginButton;
    
    @UiComponentMapping(id="id_product")
    private Select productSelect;
    
    @UiComponentMapping(name="Next ...")
    private Button nextButton;
	
	@UiComponentMapping(id="id_email")
    private TextBox emailInput;
	
	@UiComponentMapping(id="id_drop")
    private Select dropSelect;
    
    @UiComponentMapping(name="Deliver")
    private Button deliverButton;
    
    @UiComponentMapping(id="text-result")
    private UiComponent resultText;
    
	
    
	public String delivery(BrowserTab browserTab,
			String username, 
			String password,
			String type, 
			String productName, 
			String pkgName,
			String rstate,
			String email, 
			String dropName, 
			long timeOut) {
		productLink = this.getLink("#product_" + productName);
		pkgLink = this.getLink("#package_" + pkgName);
		newDeliverLink = this.getLink("#new_deliver_" + rstate);
		deliverLink = this.getLink("#deliver_" + rstate);
		
		//Complete Package List Delivery
		if(type.contains("new")){
	        cpl_link.click();
	       	browserTab.waitUntilComponentIsDisplayed(pkgLink, timeOut).click();
	       	UI.pause(2500);
	        // checking if All link exists on Package Revision Page
	        if (viewAllPkgRevs.exists()){ 
        		viewAllPkgRevs.click();
        		browserTab.waitUntilComponentIsDisplayed(newDeliverLink, timeOut).click();
        	}else{
	       		browserTab.waitUntilComponentIsDisplayed(newDeliverLink, timeOut).click();
        	}
	        browserTab.waitUntilComponentIsDisplayed(productSelect, timeOut);
	        productSelect.selectByValue(productName.replaceAll("\\s",""));
	        nextButton.click();
		} else{
	    	//Product - Package Delivery
   			productLink.click();
   			browserTab.waitUntilComponentIsDisplayed(releaseMenu, timeOut).click();
	    	browserTab.waitUntilComponentIsDisplayed(pkgsLink, timeOut).click();
	        browserTab.waitUntilComponentIsDisplayed(pkgLink, timeOut).click();
	        UI.pause(2500);
	        // checking if All link exists on Package Revision Page
	        if (viewAllPkgRevs.exists()){ 
	        	viewAllPkgRevs.click();
        		browserTab.waitUntilComponentIsDisplayed(deliverLink, timeOut).click();
        	}else{
        		browserTab.waitUntilComponentIsDisplayed(deliverLink, timeOut).click();
	        }
	    }
		browserTab.waitUntilComponentIsDisplayed(usernameInput, timeOut);
		usernameInput.setText(username);
		passwordInput.setText(password);
		loginButton.click();
		browserTab.waitUntilComponentIsDisplayed(emailInput, timeOut);
		emailInput.setText(email);
		if(dropName != null){
			for (Option d: dropSelect.getAllOptions()){
				if (d.getValue().contains(dropName.replaceAll("\\s",""))){
					dropSelect.selectByValue(dropName.replaceAll("\\s",""));
				}
			}
		}
        UI.pause(500);
		deliverButton.click();		
		return browserTab.waitUntilComponentIsDisplayed(resultText, timeOut).getText();
	}
	

	
}
