package top.wkbin.taixu.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.test.platform.app.InstrumentationRegistry
import android.graphics.Bitmap
import java.io.File
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4
import top.wkbin.taixu.core.model.ChengmingAccessibilityOptions
import top.wkbin.taixu.ui.theme.TaiXuTheme
import top.wkbin.taixu.ui.theme.ThemeStyle

/** Exercise the actual dependency at runtime, including its binary compatibility with current Compose. */
@RunWith(AndroidJUnit4::class)
class ChengmingDeviceTest {
    @get:Rule val compose = createComposeRule()

    @Test fun captureLightDarkAndLargeTextGallery() {
        var dark by mutableStateOf(false)
        var fontScale by mutableFloatStateOf(1f)
        var reduced by mutableStateOf(false)
        compose.setContent {
            val density = LocalDensity.current
            CompositionLocalProvider(LocalDensity provides Density(density.density, fontScale)) {
                TaiXuTheme(style = ThemeStyle.LIQUID_GLASS, dynamicColor = false, darkTheme = dark,
                    accessibility = ChengmingAccessibilityOptions(reduceMotion = reduced,
                        reduceTransparency = reduced, increaseContrast = reduced)) {
                    Scaffold(modifier = Modifier.fillMaxSize().testTag("galleryRoot"),
                        containerColor = androidx.compose.material3.MaterialTheme.colorScheme.background,
                        topBar = { RuntimeTopBar("澄明", statusText = "Cupertino · 清晰内容与局部玻璃", largeTitle = true) },
                        bottomBar = { RuntimeBottomBar(MainDestination.Settings, {}) }) { padding ->
                        Column(Modifier.padding(padding).padding(16.dp).verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            RuntimeGroupedList(itemCount = 3, modifier = Modifier.fillMaxWidth()) { index ->
                                Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    IconTile(RuntimeIconName.Settings, size = 30.dp)
                                    Text(listOf("模型与工具", "Linux 与工作区", "外观与无障碍")[index], Modifier.weight(1f))
                                    RuntimeIcon(RuntimeIconName.ChevronRight, Modifier.size(14.dp))
                                }
                            }
                            RuntimeSegmentedControl(listOf("标准", "增强"), 0, {})
                            RuntimeOutlinedTextField("Hello Kotlin", {}, label = { Text("输入框") })
                            Row(Modifier.fillMaxWidth(), verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                                Text("系统开关", Modifier.weight(1f)); RuntimeSwitch(true, {})
                                RuntimeCheckbox(true, {})
                            }
                            RuntimeSlider(0.5f, {})
                            RuntimeLinearProgressIndicator(progress = { 0.65f }, modifier = Modifier.fillMaxWidth())
                            RuntimeButton(onClick = {}, modifier = Modifier.fillMaxWidth()) { Text("主要操作") }
                        }
                    }
                }
            }
        }
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val folder = File(context.getExternalFilesDir(null), "ui-validation").apply { mkdirs() }
        listOf("light", "dark", "large-text-high-contrast").forEachIndexed { index, name ->
            compose.runOnIdle { dark = index != 0; fontScale = if (index == 2) 2f else 1f; reduced = index == 2 }
            compose.waitForIdle()
            File(folder, "$name.png").outputStream().use {
                requireNotNull(InstrumentationRegistry.getInstrumentation().uiAutomation.takeScreenshot()).compress(Bitmap.CompressFormat.PNG, 100, it)
            }
        }
        println("CHENGMING_GALLERY_DIR=${folder.absolutePath}")
    }

    @Test fun cupertinoControlsRenderAndAcceptInput() {
        var switchValue by mutableStateOf(false)
        var checkboxValue by mutableStateOf(false)
        var fieldValue by mutableStateOf("")
        compose.setContent {
            TaiXuTheme(style = ThemeStyle.LIQUID_GLASS, dynamicColor = false) {
                Column {
                    RuntimeSwitch(switchValue, { switchValue = it }, Modifier.testTag("switch"))
                    RuntimeCheckbox(checkboxValue, { checkboxValue = it }, Modifier.testTag("checkbox"))
                    RuntimeOutlinedTextField(fieldValue, { fieldValue = it })
                    RuntimeSlider(0.5f, {}, Modifier.testTag("slider"))
                    RuntimeCircularProgressIndicator()
                    RuntimeButton(onClick = {}, modifier = Modifier.testTag("button")) { Text("Action") }
                }
            }
        }
        compose.onNodeWithTag("switch").performClick()
        compose.onNodeWithTag("checkbox").performClick()
        compose.onNode(hasSetTextAction()).performTextInput("hello")
        compose.runOnIdle {
            assertTrue(switchValue); assertTrue(checkboxValue); assertEquals("hello", fieldValue)
        }
        compose.onNodeWithTag("button").assertHasClickAction()
        compose.onNodeWithTag("slider").assertExists()
    }

    @Test fun reducedTransparencyPreservesFourNavigationDestinations() {
        var selected = MainDestination.Settings
        compose.setContent {
            TaiXuTheme(style = ThemeStyle.LIQUID_GLASS, dynamicColor = false,
                accessibility = ChengmingAccessibilityOptions(reduceTransparency = true, reduceMotion = true)) {
                RuntimeBottomBar(selected, { selected = it })
            }
        }
        val tabs = SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Tab)
        compose.onAllNodes(tabs).assertCountEquals(4)
        compose.onAllNodes(tabs)[0].performClick()
        compose.runOnIdle { assertEquals(MainDestination.Home, selected) }
    }

    @Test fun reducedMotionControlsRetainInteractionSemantics() {
        var selected by mutableIntStateOf(0)
        var checked by mutableStateOf(false)
        compose.setContent {
            TaiXuTheme(style = ThemeStyle.LIQUID_GLASS, dynamicColor = false,
                accessibility = ChengmingAccessibilityOptions(reduceMotion = true, increaseContrast = true)) {
                Column {
                    RuntimeSegmentedControl(listOf("First", "Second"), selected, { selected = it })
                    RuntimeSwitch(checked, { checked = it }, Modifier.testTag("reducedSwitch"))
                }
            }
        }
        compose.onNodeWithText("Second").performClick()
        compose.onNodeWithTag("reducedSwitch").performClick()
        compose.runOnIdle { assertEquals(1, selected); assertTrue(checked) }
    }

    @Test fun longDialogBodyDoesNotHideConfirmation() {
        var confirmed = false
        compose.setContent {
            TaiXuTheme(style = ThemeStyle.LIQUID_GLASS, dynamicColor = false) {
                RuntimeAlertDialog(onDismissRequest = {}, title = { Text("Title") },
                    text = { Column { repeat(25) { Text("Scrollable paragraph $it") } } },
                    confirmButton = { RuntimeTextButton(onClick = { confirmed = true }) { Text("Confirm") } },
                    dismissButton = { RuntimeTextButton(onClick = {}) { Text("Cancel") } })
            }
        }
        compose.onNodeWithText("Confirm").assertIsDisplayed().performClick()
        compose.runOnIdle { assertTrue(confirmed) }
    }

    @Test fun cupertinoTopBarAndMenuUseCurrentComposeRuntime() {
        var clicked = false
        compose.setContent {
            TaiXuTheme(style = ThemeStyle.LIQUID_GLASS, dynamicColor = false) {
                Column {
                    RuntimeTopBar("Navigation", onBack = {})
                    RuntimeSegmentedControl(listOf("A", "B"), 0, {})
                    Box {
                        RuntimeDropdownMenu(expanded = true, onDismissRequest = {}) {
                            RuntimeDropdownMenuItem(text = { Text("Menu action") }, onClick = { clicked = true })
                        }
                    }
                }
            }
        }
        compose.onNodeWithText("Menu action").performClick()
        compose.runOnIdle { assertTrue(clicked) }
    }

    @Test fun largeTitleCollapsesWhenItsPageScrolls() {
        compose.setContent {
            TaiXuTheme(style = ThemeStyle.LIQUID_GLASS, dynamicColor = false) {
                val scroll = rememberRuntimeTopBarScrollState()
                Scaffold(topBar = { RuntimeTopBar("Large title", largeTitle = true, scrollState = scroll,
                    modifier = Modifier.testTag("largeBar")) }) { padding ->
                    LazyColumn(Modifier.padding(padding).testTag("scrollPage").nestedScroll(scroll.nestedScrollConnection)) {
                        items(60) { Text("Row $it", Modifier.padding(vertical = 12.dp)) }
                    }
                }
            }
        }
        val before = compose.onNodeWithTag("largeBar").fetchSemanticsNode().boundsInRoot.height
        compose.onNodeWithTag("scrollPage").performTouchInput { swipeUp() }
        compose.waitForIdle()
        val after = compose.onNodeWithTag("largeBar").fetchSemanticsNode().boundsInRoot.height
        assertTrue("Large title should collapse as the list scrolls", after < before)
    }
}
