package com.kelnik.htracker.data.mapper

import com.kelnik.htracker.data.local.model.QuestDbModel
import com.kelnik.htracker.domain.entity.Quest
import com.kelnik.htracker.domain.mapper.Mapper
import javax.inject.Inject

class QuestMapperImpl @Inject constructor(): Mapper<Quest, QuestDbModel> {
    override fun mapItemToDbModel(item: Quest): QuestDbModel {
        TODO("Not yet implemented")
    }

    override fun mapDbModelToItem(dbModel: QuestDbModel): Quest {
        TODO("Not yet implemented")
    }
}