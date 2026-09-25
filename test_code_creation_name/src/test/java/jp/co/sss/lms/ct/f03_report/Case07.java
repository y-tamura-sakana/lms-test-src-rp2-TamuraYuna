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
 * ケース07
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース07 受講生 レポート新規登録(日報) 正常系")
public class Case07 {

	//トップ画面
	final String topUrl = "http://localhost:8080/lms/";

	//コース詳細画面
	final String courseDetailUrl = "http://localhost:8080/lms/course/detail";

	//セクション詳細画面
	final String sectionUrl = "http://localhost:8080/lms/section/detail";

	//レポート登録画面
	final String reportRegistUrl = "http://localhost:8080/lms/report/regist";

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
	@DisplayName("テスト03 未提出の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() {

		//画面の最大化
		webDriver.manage().window().maximize();

		//10/5の詳細ボタンを押下
		String targetDate = "10月5日";
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
	@DisplayName("テスト04 「提出する」ボタンを押下しレポート登録画面に遷移")
	void test04() {

		By submitPath = By.xpath("//input[@type='submit' and contains(@value, 'を提出する')]");

		//待ち処理
		visibilityTimeout(submitPath, 10);

		//「日報【デモ】を提出する」を押下
		WebElement submission = webDriver
				.findElement(submitPath);
		submission.click();

		//レポート登録画面遷移の確認
		assertEquals(reportRegistUrl, webDriver.getCurrentUrl());

		//エビデンスの取得
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を入力して「提出する」ボタンを押下し確認ボタン名が更新される")
	void test05() {

		//テキストエリアをクリック
		WebElement inputText = webDriver.findElement(By.cssSelector("textarea"));
		inputText.click();

		//報告内容を入力
		String Text = "今日はテストコード演習を行った。";
		inputText.clear();
		inputText.sendKeys(Text);

		//エビデンス取得（入力）
		String suffix = "01_報告内容の入力";
		getEvidence(new Object() {
		}, suffix);

		//「提出する」ボタンを押下
		webDriver.findElement(By.cssSelector("button[type='submit']")).click();

		//セクション画面遷移の確認
		assertTrue(webDriver.getCurrentUrl().startsWith(sectionUrl));

		//エビデンスの取得（画面遷移）
		suffix = "02_セクション画面への遷移";
		getEvidence(new Object() {
		}, suffix);
	}

}
