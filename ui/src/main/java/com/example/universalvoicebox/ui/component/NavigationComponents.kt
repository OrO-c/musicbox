package com.example.universalvoicebox.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.universalvoicebox.domain.model.Section

/**
 * 底部导航组件
 */
@Composable
fun BottomNavigation(
    sections: List<Section>,
    selectedSectionId: String?,
    onSectionSelected: (String) -> Unit
) {
    NavigationBar {
        sections.forEach { section ->
            NavigationBarItem(
                selected = selectedSectionId == section.id,
                onClick = { onSectionSelected(section.id) },
                icon = { /* 可以添加图标 */ },
                label = { Text(section.name) }
            )
        }
    }
}

/**
 * 导航抽屉组件
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDrawer(
    sections: List<Section>,
    selectedSectionId: String?,
    onSectionSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "栏目",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )
                LazyColumn {
                    items(sections) { section ->
                        NavigationDrawerItem(
                            selected = selectedSectionId == section.id,
                            onClick = { onSectionSelected(section.id) },
                            label = { Text(section.name) },
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }
        },
        content = { /* 内容区域 */ }
    ) {
        // 这里应该显示主要内容
    }
}