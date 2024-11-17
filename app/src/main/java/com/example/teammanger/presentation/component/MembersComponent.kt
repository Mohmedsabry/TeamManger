package com.example.teammanger.presentation.component

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.domain.model.Member
import com.example.teammanger.R
import com.example.teammanger.util.Constants.FEMALE
import ir.kaaveh.sdpcompose.ssp
import java.util.Base64

@SuppressLint("NewApi")
@Composable
fun MemberComponent(
    member: Member,
    checked: Boolean,
    onCheckedClicked: (Boolean, Member) -> Unit
) {
    var isChecked by remember {
        mutableStateOf(checked)
    }
    Row(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.onBackground, RoundedCornerShape(10.dp)),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val decoder = Base64.getDecoder()
        AsyncImage(
            model = if (member.image == "no image") {
                if (member.gender == FEMALE) LocalContext.current.resources.getDrawable(
                    R.drawable.avatar_girl
                ) else LocalContext.current.resources.getDrawable(
                    R.drawable.avatar_boy
                )
            } else decoder.decode(
                member.image
            ),
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape),
        )
        Text(
            member.name,
            fontSize = 16.ssp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.background
        )
        Spacer(Modifier.weight(1f))
        Checkbox(
            checked = isChecked,
            onCheckedChange = {
                isChecked = it
                onCheckedClicked(it, member)
            },
            modifier = Modifier.padding(10.dp),
            colors = CheckboxDefaults.colors(
                checkedColor = Color.Green, uncheckedColor = MaterialTheme.colorScheme.background
            ),
        )
    }
}

@Composable
fun MembersComponent(
    members: List<Member>,
    selectedMembers: List<Member>,
    onCheckedClicked: (Boolean, Member) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            items(members) { member ->
                MemberComponent(
                    member = member,
                    onCheckedClicked = onCheckedClicked,
                    checked = selectedMembers.contains(member)
                )
            }
        }
    }
}