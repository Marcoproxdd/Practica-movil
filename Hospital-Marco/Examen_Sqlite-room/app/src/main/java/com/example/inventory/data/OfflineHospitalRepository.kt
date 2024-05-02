/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.inventory.data

import kotlinx.coroutines.flow.Flow

class OfflineHospitalRepository(private val hospitalDao: HospitalDao) : HospitalRepository {
    override fun getAllItemsStream(): Flow<List<Hospital>> = hospitalDao.getAllItems()

    override fun getItemStream(id: Int): Flow<Hospital?> = hospitalDao.getItem(id)

    override suspend fun insertItem(hospital: Hospital) = hospitalDao.insert(hospital)

    override suspend fun deleteItem(hospital: Hospital) = hospitalDao.delete(hospital)

    override suspend fun updateItem(hospital: Hospital) = hospitalDao.update(hospital)
}
