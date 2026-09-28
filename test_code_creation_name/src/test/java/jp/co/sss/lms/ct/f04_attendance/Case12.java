package jp.co.sss.lms.ct.f04_attendance;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

/**
 * 結合テスト 勤怠管理機能
 * ケース12
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース12 受講生 勤怠直接編集 入力チェック")
public class Case12 {

	//トップ画面
	final String topUrl = "http://localhost:8080/lms/";

	//コース詳細画面
	final String courseDetailUrl = "http://localhost:8080/lms/course/detail";

	//勤怠管理画面
	final String attendanceUrl = "http://localhost:8080/lms/attendance/detail";

	//勤怠直接変更画面
	final String attendanceUpdateUrl = "http://localhost:8080/lms/attendance/update";

	//画面下部
	final String down = "10000";

	//画面上部
	final String top = "0";

	/** 前処理 */
	@BeforeAll
	static void before() {
		createDriver();
	}

	/** 後処理 */
	@AfterAll
	static void after() {
		closeDriver();
	}

	@Test
	@Order(1)
	@DisplayName("テスト01 トップページURLでアクセス")
	void test01() {
		goTo(topUrl);

		//ログイン画面遷移の確認処理
		assertEquals(topUrl, webDriver.getCurrentUrl());

		//エビデンス取得
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() {
		WebElement id = webDriver.findElement(By.id("loginId"));
		id.clear();
		id.sendKeys("StudentAA01");

		WebElement password = webDriver.findElement(By.id("password"));
		password.clear();
		password.sendKeys("StudentAA0101");

		//ログインボタン押下
		webDriver.findElement(By.cssSelector(".btn.btn-primary")).click();

		//画面遷移の確認
		assertEquals(courseDetailUrl, webDriver.getCurrentUrl());

		//ログインアカウントの確認
		WebElement userElement = webDriver.findElement(By.partialLinkText("ようこそ"));
		assertEquals("ようこそ受講生ＡＡ１さん", userElement.getText());

		//エビデンスの取得
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「勤怠」リンクから勤怠管理画面に遷移")
	void test03() {
		//上部メニューの「勤怠」をクリック
		webDriver.findElement(By.partialLinkText("勤怠")).click();

		//未入力アラートが表示されたら、「OK」をクリック
		Alert alert = webDriver.switchTo().alert();
		alert.accept();

		//エビデンスの取得
		getEvidence(new Object() {
		});

		//ユーザー詳細画面に遷移できているか確認
		assertEquals(attendanceUrl, webDriver.getCurrentUrl());
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「勤怠情報を直接編集する」リンクから勤怠情報直接変更画面に遷移")
	void test04() {
		//「勤怠情報を直接変更する」リンクをクリック
		webDriver.findElement(By.partialLinkText("勤怠情報を直接編集する")).click();

		//エビデンス取得（遷移）
		getEvidence(new Object() {
		});

		//勤怠情報直接変更画面に遷移できているか確認
		assertEquals(attendanceUpdateUrl, webDriver.getCurrentUrl());
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 不適切な内容で修正してエラー表示：出退勤の（時）と（分）のいずれかが空白")
	void test05() {
		// 該当日程の箇所の出退勤登録時間を編集
		//出勤
		WebElement startHour = webDriver.findElement(By.id("startHour1"));
		Select startHourDropdown = new Select(startHour);
		startHourDropdown.selectByValue("9");

		WebElement startMinute = webDriver.findElement(By.id("startMinute1"));
		Select startMinuteDropdown = new Select(startMinute);
		startMinuteDropdown.selectByValue("");

		//退勤
		WebElement endHour = webDriver.findElement(By.id("endHour1"));
		Select endHourDropdown = new Select(endHour);
		endHourDropdown.selectByValue("");

		WebElement endMinute = webDriver.findElement(By.id("endMinute1"));
		Select endMinuteDropdown = new Select(endMinute);
		endMinuteDropdown.selectByValue("0");

		//ページ下部へスクロール
		scrollTo(down);

		//「更新」ボタン押下
		webDriver.findElement(By.cssSelector("input[type='submit'][value='更新']")).click();

		//確認ダイアログで「OK」ボタンを押下
		webDriver.switchTo().alert().accept();

		//エビデンスの取得（エラー表示）
		getEvidence(new Object() {
		});

		//エラー表示の確認
		WebElement errorMsg = webDriver.findElement(By.cssSelector(".help-inline.error"));
		assertTrue(errorMsg.isDisplayed());
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 不適切な内容で修正してエラー表示：出勤が空白で退勤に入力あり")
	void test06() {
		// 該当日程の箇所の出退勤登録時間を編集
		//出勤
		WebElement startHour = webDriver.findElement(By.id("startHour1"));
		Select startHourDropdown = new Select(startHour);
		startHourDropdown.selectByValue("");

		WebElement startMinute = webDriver.findElement(By.id("startMinute1"));
		Select startMinuteDropdown = new Select(startMinute);
		startMinuteDropdown.selectByValue("");

		//退勤
		WebElement endHour = webDriver.findElement(By.id("endHour1"));
		Select endHourDropdown = new Select(endHour);
		endHourDropdown.selectByValue("18");

		WebElement endMinute = webDriver.findElement(By.id("endMinute1"));
		Select endMinuteDropdown = new Select(endMinute);
		endMinuteDropdown.selectByValue("0");

		//ページ下部へスクロール
		scrollTo(down);

		//「更新」ボタン押下
		webDriver.findElement(By.cssSelector("input[type='submit'][value='更新']")).click();

		//確認ダイアログで「OK」ボタンを押下
		webDriver.switchTo().alert().accept();

		//エビデンスの取得（エラー表示）
		getEvidence(new Object() {
		});

		//エラー表示の確認
		WebElement errorMsg = webDriver.findElement(By.cssSelector(".help-inline.error"));
		assertTrue(errorMsg.isDisplayed());
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 不適切な内容で修正してエラー表示：出勤が退勤よりも遅い時間")
	void test07() {
		// 該当日程の箇所の出退勤登録時間を編集
		//出勤
		WebElement startHour = webDriver.findElement(By.id("startHour1"));
		Select startHourDropdown = new Select(startHour);
		startHourDropdown.selectByValue("18");

		WebElement startMinute = webDriver.findElement(By.id("startMinute1"));
		Select startMinuteDropdown = new Select(startMinute);
		startMinuteDropdown.selectByValue("0");

		//退勤
		WebElement endHour = webDriver.findElement(By.id("endHour1"));
		Select endHourDropdown = new Select(endHour);
		endHourDropdown.selectByValue("9");

		WebElement endMinute = webDriver.findElement(By.id("endMinute1"));
		Select endMinuteDropdown = new Select(endMinute);
		endMinuteDropdown.selectByValue("0");

		//ページ下部へスクロール
		scrollTo(down);

		//「更新」ボタン押下
		webDriver.findElement(By.cssSelector("input[type='submit'][value='更新']")).click();

		//確認ダイアログで「OK」ボタンを押下
		webDriver.switchTo().alert().accept();

		//エビデンスの取得（エラー表示）
		getEvidence(new Object() {
		});

		//エラー表示の確認
		WebElement errorMsg = webDriver.findElement(By.cssSelector(".help-inline.error"));
		assertTrue(errorMsg.isDisplayed());
	}

	@Test
	@Order(8)
	@DisplayName("テスト08 不適切な内容で修正してエラー表示：出退勤時間を超える中抜け時間")
	void test08() {
		// 該当日程の箇所の出退勤登録時間を編集
		//出勤
		WebElement startHour = webDriver.findElement(By.id("startHour1"));
		Select startHourDropdown = new Select(startHour);
		startHourDropdown.selectByValue("9");

		WebElement startMinute = webDriver.findElement(By.id("startMinute1"));
		Select startMinuteDropdown = new Select(startMinute);
		startMinuteDropdown.selectByValue("0");

		//退勤
		WebElement endHour = webDriver.findElement(By.id("endHour1"));
		Select endHourDropdown = new Select(endHour);
		endHourDropdown.selectByValue("12");

		WebElement endMinute = webDriver.findElement(By.id("endMinute1"));
		Select endMinuteDropdown = new Select(endMinute);
		endMinuteDropdown.selectByValue("0");

		//中抜け時間
		WebElement blankTime = webDriver.findElement(By.name("attendanceList[1].blankTime"));
		Select blankTimeDropdown = new Select(blankTime);
		blankTimeDropdown.selectByValue("465");

		//ページ下部へスクロール
		scrollTo(down);

		//「更新」ボタン押下
		webDriver.findElement(By.cssSelector("input[type='submit'][value='更新']")).click();

		//確認ダイアログで「OK」ボタンを押下
		webDriver.switchTo().alert().accept();

		//エビデンスの取得（エラー表示）
		getEvidence(new Object() {
		});

		//エラー表示の確認
		WebElement errorMsg = webDriver.findElement(By.cssSelector(".help-inline.error"));
		assertTrue(errorMsg.isDisplayed());
	}

	@Test
	@Order(9)
	@DisplayName("テスト09 不適切な内容で修正してエラー表示：備考が100文字超")
	void test09() {
		// 該当日程の箇所の出退勤登録時間を編集
		//出勤
		WebElement startHour = webDriver.findElement(By.id("startHour1"));
		Select startHourDropdown = new Select(startHour);
		startHourDropdown.selectByValue("9");

		WebElement startMinute = webDriver.findElement(By.id("startMinute1"));
		Select startMinuteDropdown = new Select(startMinute);
		startMinuteDropdown.selectByValue("0");

		//退勤
		WebElement endHour = webDriver.findElement(By.id("endHour1"));
		Select endHourDropdown = new Select(endHour);
		endHourDropdown.selectByValue("18");

		WebElement endMinute = webDriver.findElement(By.id("endMinute1"));
		Select endMinuteDropdown = new Select(endMinute);
		endMinuteDropdown.selectByValue("0");

		//備考
		WebElement note = webDriver.findElement(By.name("attendanceList[1].note"));
		note.click();
		note.clear();
		note.sendKeys("あ".repeat(101));

		//ページ下部へスクロール
		scrollTo(down);

		//「更新」ボタン押下
		webDriver.findElement(By.cssSelector("input[type='submit'][value='更新']")).click();

		//確認ダイアログで「OK」ボタンを押下
		webDriver.switchTo().alert().accept();

		//エビデンスの取得（エラー表示）
		getEvidence(new Object() {
		});

		//エラー表示の確認
		WebElement errorMsg = webDriver.findElement(By.cssSelector(".help-inline.error"));
		assertTrue(errorMsg.isDisplayed());
	}

}
