package jp.co.sss.lms.ct.f04_attendance;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

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

/**
 * 結合テスト 勤怠管理機能
 * ケース11
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース11 受講生 勤怠直接編集 正常系")
public class Case11 {

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
	@DisplayName("テスト05 すべての研修日程の勤怠情報を正しく更新し勤怠管理画面に遷移")
	void test05() {
		//全ての研修日の「定時」ボタンをクリック
		List<WebElement> attendance = webDriver.findElements(By.cssSelector("td.w60 button[type='button']"));

		for (WebElement keepTime : attendance) {
			keepTime.click();
		}

		pageLoadTimeout(5);

		//画面下部へスクロール
		scrollTo(down);

		//「更新」ボタンをクリック
		webDriver.findElement(By.cssSelector("input[type='submit'][value='更新']")).click();

		//確認ダイアログが表示されたら、「OK」をクリック
		Alert alert = webDriver.switchTo().alert();
		alert.accept();

		//エビデンスの取得（遷移）
		getEvidence(new Object() {
		});

		//勤怠管理画面に遷移できたか確認
		assertEquals(attendanceUpdateUrl, webDriver.getCurrentUrl());

	}

}
