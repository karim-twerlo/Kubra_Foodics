package com.fooder.Pages;

import com.fooder.PageBase.PageBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;

public class P016LiveOrder extends PageBase {
    public P016LiveOrder(WebDriver driver) {
        super(driver);
    }
    private final By Live_Order_From_Menu = By.xpath("//span[normalize-space()='Live orders' or contains(text(),'الطلبات الحية')]");
    private final By Create_Order = By.xpath("//span[normalize-space()='Create order' or contains(text(),'إنشاء طلب')]");
    private final By Active_Step = By.xpath("//div[@class='step active']");
    private final By Order_Details = By.xpath("//div[@data-target='#order-details']");
    private final By payment_Tap = By.xpath("//div[@data-target='#create-order']");
    private final By Input_Mobile_Number = By.xpath("//input[@class='form-control phoneInput-number_contact ng-untouched ng-pristine ng-valid']");
    private final By Input_Full_Name = By.xpath("//form[@class='ng-untouched ng-pristine ng-invalid']//input[@id='full_name']");
    private final By Select_Branch_Dropdown = By.xpath("//ng-select[@formcontrolname='branch']//input[@type='text']");
    private final By Go_Select_Item = By.xpath("//button[normalize-space()='Go to items' or contains(text(),'اذهب لاختيار العناصر')]");
    private final By Basket= By.xpath("//cart-summary//h2[contains(text(),'My basket') or contains(text(),'سلتي')]");
    private final By Cart_Items= By.xpath("//cart-summary//div[@class='cart-items']");
    private final By Place_Holder_Special_Instructions = By.xpath("//textarea[@placeholder='Without BBQ' or @placeholder='بدون باربيكيو']");
    private final By Search_Btn = By.xpath("//input[@placeholder='Search' or @placeholder='أبحث']");
    private final By Special_Instructions_Label= By.xpath("//form[@class='ng-untouched ng-pristine ng-valid']//p[contains(text(),'Special instructions') or contains(text(),'تعليمات خاصة')]");
    private final By Sum = By.xpath("//div[@class='cart-bill_details']//div//p[contains(text(),'Grand Total') or contains(text(),'المجموع الإجمالي')]");
    private final By Product = By.xpath("//button[@class='btn categoryBtn']//img");
    private final By Add_To_Basket = By.xpath("//span[normalize-space()='Add to basket' or contains(text(),'اضف للسلة')]");
    private final By Quantity = By.xpath("//span[normalize-space()='Quantity' or contains(text(),'الكمية')]");
    private final By Plus = By.xpath("//button[@class='plusBtn']//*[name()='svg']");
    private final By Plus_into_Card = By.xpath("//cart-summary//button[@class='plusBtn']");
    private final By Complete_Order = By.xpath("//button[normalize-space()='Go to checkout' or contains(text(),'اكمل الطلب')]");
    private final By Place_Order = By.xpath("//button[normalize-space()='Create order' or contains(text(),'انشى الطلب')]");
    private final By Amount_Place_Holder = By.xpath("//cart-summary//div[@class='cart-items']//p[contains(text(),' SAR')]");
    private final By Order_Created_Msg =By.xpath("//h5[contains(text(),'You have') or contains(text(),'لديك')]");
    private final By Close_Order_Msg = By.xpath("//button[contains(text(),'close') or contains(text(),'إغلاق')]");
    private void checkLiveOrderScreen(){
        navigateToLiveOrderScreen();
        clickOnelement(Live_Order_From_Menu);
        Assert.assertTrue(assertElementDisplayed(Create_Order));

    }
    private void navigateToLiveOrderScreen(){
        scrollToElement(Live_Order_From_Menu);
        Assert.assertTrue(assertElementDisplayed(Live_Order_From_Menu));
    }
    private void checkCreateLiveOrderScreen(){
        Assert.assertTrue(assertElementDisplayed(Active_Step));
        Assert.assertTrue(checkForLocalization(Active_Step,"Ordering Service","خدمة الطلب"));
        Assert.assertTrue(checkForLocalization(Order_Details,"Order Cart","تسوق الطلب"));
        Assert.assertTrue(checkForLocalization(payment_Tap,"Checkout","دفع الطلب"));
        Assert.assertTrue(assertElementDisplayed(Input_Full_Name));
        Assert.assertTrue(assertElementDisplayed(Input_Mobile_Number));

    }
    public void checkLiveOrderCreation(String fullName , String mobileNumber , String branchName , String Category , String Product , int NumberOfProducts){
        checkLiveOrderScreen();
        clickOnelement(Create_Order);
        checkCreateLiveOrderScreen();
        fillOrderForm(fullName , mobileNumber , branchName);
        confirmFillOrder();
        validateOrderCartScreen(Category ,Product , NumberOfProducts );
    }
    private void SelectBranch(String text){
        driver.findElement(Select_Branch_Dropdown).click();
        By optionLocator = By.xpath("//div[@class='ng-option ng-option-marked']/span[@class='ng-option-label'][contains(text(),'" + text + "')]");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement optionElement = wait.until(ExpectedConditions.elementToBeClickable(optionLocator));
        optionElement.click();

    }
    private void fillOrderForm(String FullName , String MobileNumber , String BranchName){
        sendTextToInputField(FullName , Input_Full_Name);
        sendTextToInputField(MobileNumber , Input_Mobile_Number);
        scrollToElement(Select_Branch_Dropdown);
        SelectBranch(BranchName);

    }
    private void confirmFillOrder(){
        scrollToElement(Go_Select_Item);
        Assert.assertTrue(assertElementDisplayed(Go_Select_Item));
        clickOnelement(Go_Select_Item);
    }
    private void validateOrderCartScreen(String category , String ProductName , int numberOfProducts){
        By Category = By.xpath("//button[normalize-space()='" + category + "']");
        By product = By.xpath("//h4[normalize-space()='" + ProductName + "']");
        Assert.assertTrue(assertElementDisplayed(Category));
        validatePaymentScreen();
        selectProductFromCategory(Category , Product);
        validateBasketScreen(product);
        clickOnelement(Add_To_Basket);
        validateOrderIntoCard(numberOfProducts);
        clickOnelement(Complete_Order);
        validatePlaceOrderScreen();
        clickOnelement(Place_Order);
        vlidateOrderCratedSccessfully();
        validateOrders();

    }
    private void validateEachItem(By by){
        scrollToElement(by);
        Assert.assertTrue(assertElementDisplayed(by));
    }
    private void selectNumberOfProducts(int Count){
        int one_item_cost = extractAndConvertToInt(driver.findElement(Amount_Place_Holder).getText());
        for(int i = 1 ; i < Count ; i++){
            clickOnelement(Plus_into_Card);
        }
        int total_Cost = extractAndConvertToInt(driver.findElement(Amount_Place_Holder).getText());
        Assert.assertEquals(Count, total_Cost / one_item_cost);
    }
    private void sendSpecialInstructions(String instructions){
        scrollToElement(Place_Holder_Special_Instructions);
        sendTextToInputField(instructions , Place_Holder_Special_Instructions);
    }
    private void validatePaymentScreen(){
        validateEachItem(Basket);
        validateEachItem(Cart_Items);
        validateEachItem(Place_Holder_Special_Instructions);
        validateEachItem(Search_Btn);
        validateEachItem(Special_Instructions_Label);
        validateEachItem(Sum);
    }
    private void selectProductFromCategory(By Category , By Product){
        scrollToElement(Category);
        clickOnelement(Category);
        Assert.assertTrue(assertElementDisplayed(Product));
        clickOnelement(Product);
    }
    private void validateBasketScreen(By Product){
        Assert.assertTrue(assertElementDisplayed(Add_To_Basket));
        Assert.assertTrue(assertElementDisplayed(Quantity));
        Assert.assertTrue(assertElementDisplayed(Plus));
        Assert.assertTrue(assertElementDisplayed(Product));
    }
    private void validatePlaceOrderScreen(){
        scrollUp();
        Assert.assertTrue(checkForLocalization(Active_Step,"Checkout","دفع الطلب"));
        scrollToElement(Place_Order);
        Assert.assertTrue(assertElementDisplayed(Place_Order));
    }
    private void validateOrderIntoCard(int count){
        selectNumberOfProducts(count);
        sendSpecialInstructions("test");
        scrollToElement(Complete_Order);
        Assert.assertTrue(assertElementDisplayed(Complete_Order));
    }
    private void vlidateOrderCratedSccessfully(){
        Assert.assertTrue(assertElementDisplayed(Order_Created_Msg));
        Assert.assertTrue(assertElementDisplayed(Close_Order_Msg));
        clickOnelement(Close_Order_Msg);
    }
    public void validateOrders(){
        Assert.assertTrue(assertElementDisplayed(Create_Order));
        List<WebElement> checkboxElements = driver.findElements(By.xpath("//p[normalize-space()='Order No' or contains(text(),'رقم الطلب')]"));
        for(int i = 0 ; i < checkboxElements.size() ; i++){
            System.out.println("======>> " + checkboxElements.get(i).getText());
        }
        System.out.println(checkboxElements.size());

    }

}
