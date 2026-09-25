package jp.co.sss.lms.ct.f03_report;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

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
 * 結合テスト レポート機能
 * ケース08
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース08 受講生 レポート修正(週報) 正常系")
public class Case08 {

	//トップ画面
	final String topUrl = "http://localhost:8080/lms/";

	//コース詳細画面
	final String courseDetailUrl = "http://localhost:8080/lms/course/detail";

	//セクション詳細画面
	final String sectionUrl = "http://localhost:8080/lms/section/detail";

	//レポート登録画面
	final String reportRegistUrl = "http://localhost:8080/lms/report/regist";

	//ユーザー詳細画面
	final String userdetailUrl = "http://localhost:8080/lms/user/detail";

	//レポート詳細画面
	final String reportDetailUrl = "http://localhost:8080/lms/report/detail";

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

		//入力値の入力
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

		//エビデンスの取得
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 提出済の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() {

		//画面の最大化
		webDriver.manage().window().maximize();

		//10/5の詳細ボタンを押下
		String targetDate = "10月2日";
		WebElement detailButton = webDriver
				.findElement(By.xpath("//tr[td[contains(text(),'" + targetDate + "')]]//input[@value='詳細']"));
		detailButton.click();

		//セクション画面遷移の確認
		assertEquals(sectionUrl, webDriver.getCurrentUrl());

		//エビデンスの取得
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「確認する」ボタンを押下しレポート登録画面に遷移")
	void test04() {

		By checkPath = By.xpath("//input[@type='submit' and contains(@value, '週報') and contains(@value, '確認する')]");

		//待ち処理
		visibilityTimeout(checkPath, 5);

		//「週報【デモ】を提出する」を押下
		scrollTo("3000");
		WebElement submission = webDriver.findElement(checkPath);
		submission.click();

		//レポート登録画面遷移の確認
		assertEquals(reportRegistUrl, webDriver.getCurrentUrl());

		//エビデンスの取得
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を修正して「提出する」ボタンを押下しセクション詳細画面に遷移")
	void test05() {

		//以下が編集内容
		//学習項目
		By fieldName = By.id("intFieldName_0");
		visibilityTimeout(fieldName, 3);
		webDriver.findElement(fieldName).clear();
		webDriver.findElement(fieldName).sendKeys("ITリテラシー②");

		//理解度
		By fieldValue = By.id("intFieldValue_0");
		webDriver.findElement(fieldValue).sendKeys("1");

		//ページ下部へスクロール
		scrollTo("3000");

		String[] reportValues = {
				"1", //目標の達成度
				"週報編集のテストです。", //所感
				"週報編集のテストです。" //１週間の振り返り
		};

		// ループで上記を入力
		for (int i = 0; i < reportValues.length; i++) {
			WebElement textArea = webDriver.findElement(By.id("content_" + i));
			textArea.clear();
			textArea.sendKeys(reportValues[i]);
		}

		//エビデンスの取得（入力）
		String suffix = "01_週報の編集内容を入力";
		getEvidence(new Object() {
		}, suffix);

		//提出するボタンをクリック
		webDriver.findElement(By.cssSelector("button[type='submit']")).click();

		//エビデンス取得（遷移）
		suffix = "02_「提出する」ボタン押下処理";
		getEvidence(new Object() {
		}, suffix);

		//セッション詳細画面へ遷移できたか確認
		assertTrue(webDriver.getCurrentUrl().startsWith(sectionUrl));

	}

	@Test
	@Order(6)
	@DisplayName("テスト06 上部メニューの「ようこそ○○さん」リンクからユーザー詳細画面に遷移")
	void test06() {

		//上部メニューの「ようこそ●●さん」をクリック
		webDriver.findElement(By.partialLinkText("ようこそ")).click();

		//エビデンスの取得
		getEvidence(new Object() {
		});

		//ユーザー詳細画面に遷移できているか確認
		assertEquals(userdetailUrl, webDriver.getCurrentUrl());
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 該当レポートの「詳細」ボタンを押下しレポート詳細画面で修正内容が反映される")
	void test07() {

		// 該当日付の「詳細」ボタンをクリック
		scrollTo("3000"); //ページ下部までスクロール

		By reportBy = By.xpath("//tr[contains(., '10月2日') and contains(., '週報')]//input[@value='詳細']");
		webDriver.findElement(reportBy).click();

		//レポート詳細画面に遷移できているか確認
		assertEquals(reportDetailUrl, webDriver.getCurrentUrl());

		//編集内容が反映さえれているか確認
		//編集内容
		String[] inputText = {
				"ITリテラシー②", //0_学習内容
				"1", //1_理解度
				"1", //2_目標達成度
				"週報編集のテストです。", //3_所感
				"週報編集のテストです。" //4_一週間の振り返り
		};

		//学習理解度の確認
		WebElement table = webDriver.findElement(By.xpath("//h3[text()='学習理解度']/following-sibling::table"));
		String tableText = table.getText();
		assertTrue(tableText.contains(inputText[0]));
		assertTrue(tableText.contains(inputText[1]));

		//報告レポートの確認
		WebElement reportText = webDriver.findElement(By.xpath("//h3[text()='報告レポート']/following-sibling::table"));
		String text = reportText.getText();
		visibilityTimeout(By.xpath("//h3[text()='報告レポート']/following-sibling::table"), 5);
		assertTrue(text.contains(inputText[2]));
		assertTrue(text.contains(inputText[3]));
		assertTrue(text.contains(inputText[4]));

		//エビデンス取得
		getEvidence(new Object() {
		});
	}

}
