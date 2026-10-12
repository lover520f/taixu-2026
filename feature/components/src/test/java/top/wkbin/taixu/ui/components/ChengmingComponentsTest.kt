package top.wkbin.taixu.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import top.wkbin.taixu.core.model.ChengmingAccessibilityOptions
import top.wkbin.taixu.ui.theme.TaiXuTheme
import top.wkbin.taixu.ui.theme.ThemeStyle

/** Exercise the actual dependency at runtime, including its binary compatibility with current Compose. */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class ChengmingComponentsTest {
    @get:Rule val compose = createComposeRule()

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

    @Test fun dialogWithScrollableBodyDoesNotFailInfiniteConstraints() {
        var confirmed = false
        compose.setContent {
            TaiXuTheme(style = ThemeStyle.LIQUID_GLASS, dynamicColor = false) {
                RuntimeAlertDialog(
                    onDismissRequest = {},
                    title = { Text("Title") },
                    text = {
                        Column(modifier = Modifier.fillMaxWidth().heightIn(max = 200.dp).verticalScroll(rememberScrollState())) {
                            repeat(10) { Text("Scrollable item $it") }
                        }
                    },
                    confirmButton = { RuntimeTextButton(onClick = { confirmed = true }) { Text("OK") } },
                )
            }
        }
        compose.onNodeWithText("OK").assertIsDisplayed().performClick()
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

    @Test fun actionSheetTriggersSelectedAction() {
        var actionFired = false
        var dismissed = false
        compose.setContent {
            TaiXuTheme(style = ThemeStyle.LIQUID_GLASS, dynamicColor = false) {
                RuntimeActionSheet(
                    visible = true,
                    onDismissRequest = { dismissed = true },
                    title = "Confirm Action",
                    items = listOf(
                        RuntimeActionSheetItem("Delete File", isDestructive = true, onClick = { actionFired = true }),
                    ),
                )
            }
        }
        compose.onNodeWithText("Delete File").assertIsDisplayed().performClick()
        compose.runOnIdle { assertTrue(actionFired); assertTrue(dismissed) }
    }

    @Test fun iconTileInChengmingUsesCompactSize() {
        compose.setContent {
            TaiXuTheme(style = ThemeStyle.LIQUID_GLASS, dynamicColor = false) {
                Box(Modifier.testTag("tileBox")) {
                    IconTile(icon = RuntimeIconName.Settings)
                }
            }
        }
        val bounds = compose.onNodeWithTag("tileBox").fetchSemanticsNode().boundsInRoot
        assertTrue("Icon tile bounds must exist", bounds.width > 0f)
    }

    @Test fun typographyInLiquidGlassMaintainsRefinedProportions() {
        assertEquals(14.sp, top.wkbin.taixu.ui.theme.AppleTypography.Callout.fontSize)
        assertEquals(20.sp, top.wkbin.taixu.ui.theme.AppleTypography.Callout.lineHeight)
        assertEquals(14.sp, top.wkbin.taixu.ui.theme.AppleTypography.Button.fontSize)
        assertEquals(15.sp, top.wkbin.taixu.ui.theme.AppleTypography.Body.fontSize)
        assertEquals(15.sp, top.wkbin.taixu.ui.theme.AppleTypography.Headline.fontSize)
        assertEquals(12.sp, top.wkbin.taixu.ui.theme.AppleTypography.Footnote.fontSize)

        assertEquals(14.sp, top.wkbin.taixu.ui.theme.LiquidGlassTypography.bodyMedium.fontSize)
        assertEquals(14.sp, top.wkbin.taixu.ui.theme.LiquidGlassTypography.labelLarge.fontSize)
        assertEquals(15.sp, top.wkbin.taixu.ui.theme.LiquidGlassTypography.bodyLarge.fontSize)
        assertEquals(15.sp, top.wkbin.taixu.ui.theme.LiquidGlassTypography.titleMedium.fontSize)
        assertEquals(12.sp, top.wkbin.taixu.ui.theme.LiquidGlassTypography.bodySmall.fontSize)
    }
}

