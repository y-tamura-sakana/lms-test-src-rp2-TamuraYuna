package jp.co.sss.lms.ct.f05_exam;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * 結合テスト 試験実施機能
 * ケース14
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース13 受講生 試験の実施 結果50点")
public class Case14 {

	/** テスト07およびテスト08 試験実施日時 */
	static Date date;

	//トップ画面
	final String topUrl = "http://localhost:8080/lms/";

	//コース詳細画面
	final String courseDetailUrl = "http://localhost:8080/lms/course/detail";

	//セクション詳細画面
	final String sectionDetailUrl = "http://localhost:8080/lms/section/detail";

	//試験開始画面
	final String examStartUrl = "http://localhost:8080/lms/exam/start";

	//試験問題画面
	final String examQuestionUrl = "http://localhost:8080/lms/exam/question";

	//試験回答確認画面
	final String examCheckUrl = "http://localhost:8080/lms/exam/answerCheck";

	//試験結果確認画面
	final String examResult = "http://localhost:8080/lms/exam/result";

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
	@DisplayName("テスト03 「試験有」の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() {
		//「詳細」ボタンをクリック
		webDriver.findElement(By.xpath("//tr[td[contains(., '試験有')]]//input[@type='submit'][@value='詳細']"))
				.click();

		//エビデンス取得（遷移）
		getEvidence(new Object() {
		});

		//セクション詳細画面に遷移しているか確認
		assertEquals(sectionDetailUrl, webDriver.getCurrentUrl());
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「本日の試験」エリアの「詳細」ボタンを押下し試験開始画面に遷移")
	void test04() {
		//「詳細」ボタンをクリック
		webDriver.findElement(By.cssSelector("input[type='submit'][value='詳細']")).click();

		//エビデンス取得（遷移）
		getEvidence(new Object() {
		});

		//セクション詳細画面に遷移しているか確認
		assertEquals(examStartUrl, webDriver.getCurrentUrl());
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 「試験を開始する」ボタンを押下し試験問題画面に遷移")
	void test05() {
		// 「試験を開始する」ボタンをクリック
		webDriver.findElement(By.cssSelector("input[type='submit'][value='試験を開始する']")).click();

		//エビデンス取得（遷移）
		getEvidence(new Object() {
		});

		//試験問題画面に遷移できているか確認
		assertEquals(examQuestionUrl, webDriver.getCurrentUrl());
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 正答と誤答が半々で「確認画面へ進む」ボタンを押下し試験回答確認画面に遷移")
	void test06() {
		// 正答が半々の回答を入力
		//１問目
		webDriver.findElement(By.id("answer-0-2")).click();
		scrollBy("340");

		//２問目
		webDriver.findElement(By.id("answer-1-2")).click();
		scrollBy("340");

		//３問目
		webDriver.findElement(By.id("answer-2-0")).click();
		scrollBy("340");

		//４問目
		webDriver.findElement(By.id("answer-3-0")).click();
		scrollBy("340");

		//５問目
		webDriver.findElement(By.id("answer-4-1")).click();
		scrollBy("340");

		//６問目
		webDriver.findElement(By.id("answer-5-1")).click();

		//画面下部までスクロール
		scrollTo(down);

		//「確認画面へ進む」ボタンをクリック
		webDriver.findElement(By.cssSelector("input[type='submit'][value='確認画面へ進む']")).click();

		//エビデンス取得
		getEvidence(new Object() {
		});

		//試験回答確認画面へ遷移したか確認する
		assertEquals(examCheckUrl, webDriver.getCurrentUrl());
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 「回答を送信する」ボタンを押下し試験結果画面に遷移")
	void test07() throws InterruptedException {
		//画面下部までスクロール
		scrollTo(down);

		//回答時間によるエラー回避
		Thread.sleep(3000);

		//「回答を送信する」ボタンを押下
		webDriver.findElement(By.id("sendButton")).click();

		//確認ダイアログの「OK」ボタンをクリック
		webDriver.switchTo().alert().accept();

		//エビデンスを取得
		getEvidence(new Object() {
		});

		//試験結果画面へ遷移したか確認する
		assertEquals(examResult, webDriver.getCurrentUrl());

	}

	@Test
	@Order(8)
	@DisplayName("テスト08 「戻る」ボタンを押下し試験開始画面に遷移後当該試験の結果が反映される")
	void test08() {
		//画面下部までスクロール
		scrollTo(down);

		//「戻る」ボタンが表示されるまで待機
		By backButtonLocatorBy = By.cssSelector("input[type='submit'][value='戻る']");
		visibilityTimeout(backButtonLocatorBy, 15);

		//「戻る」ボタンを押下
		webDriver.findElement(backButtonLocatorBy).click();

		scrollBy("350");

		//「過去の試験結果」の要素が表示されるまで待機
		By resultLocator = By.xpath("//h3[contains(text(), '過去の試験結果')]/following-sibling::table[1]//tr[3]/td[2]");
		visibilityTimeout(resultLocator, 10);

		//エビデンスを取得
		getEvidence(new Object() {
		});

		//試験開始画面へ遷移したか確認する
		assertEquals(examStartUrl, webDriver.getCurrentUrl());

		//50点であるか確認する
		WebElement resultElemnt = webDriver.findElement(resultLocator);
		assertEquals("50.0点", resultElemnt.getText());
	}

}
