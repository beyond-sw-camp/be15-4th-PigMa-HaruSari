<template>
  <Header />

  <div class="category-main">
    <div class="category-header">
      <h2>카테고리 관리</h2>
      <button @click="showModal = true">+ 카테고리 추가</button>
    </div>

    <div v-if="categories.length === 0" class="empty">등록된 카테고리가 없습니다.</div>

    <div class="category-list">
      <div class="category-card"
           v-for="(category, index) in categories"
           :key="index"
           @click="openCategoryActionModal(category)">
        <div class="card-header">
          <span class="icon">{{ category.icon || '📂' }}</span>
          <span class="title">{{ category.title }}</span>
        </div>
        <div class="card-footer">
          <span class="tag" :style="{ backgroundColor: category.color }"></span>
          <span class="status">{{ category.completed ? '✅ 완료' : '⏳ 진행 중' }}</span>
        </div>
      </div>
    </div>

    <CategoryActionModal
        v-if="showActionModal"
        :category="selectedCategory"
        @close="showActionModal = false"
        @edit="handleEdit"
        @delete="handleDeleteClick"
        @complete="handleComplete"
        @cancel-complete="handleCancelComplete"
    />

    <CategoryCreateModal
        v-if="showModal"
        :isEdit="isEdit"
        :editData="editTarget"
        @close="handleModalClose"
        @created="loadCategories"
        @updated="loadCategories"
    />

    <ConfirmWithInputModal
        v-if="showDeleteModal"
        title="카테고리를 삭제할까요?"
        :message="'해당 카테고리에 연결된 모든 할 일이 함께 삭제됩니다. \n 아래 문장을 입력해야 삭제할 수 있습니다. \n\n \'카테고리를 삭제하겠습니다\''"
        requiredText="카테고리를 삭제하겠습니다"
        @close="showDeleteModal = false"
        @confirm="confirmDeleteCategory"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import Header from "@/components/layout/Header.vue";
import CategoryCreateModal from '@/features/category/components/CategoryCreateModal.vue';
import CategoryActionModal from "@/features/category/components/CategoryActionModal.vue";
import { fetchCategory } from '@/features/main/mainApi';
import { createCategory,completeCategory, deleteCategory } from '@/features/category/categoryApi.js';
import ConfirmWithInputModal from "@/components/common/ConfirmWithInputModal.vue";

const showModal = ref(false);
const categories = ref([]);
const selectedCategory = ref(null);
const showActionModal = ref(false);
const isEdit = ref(false);
const editTarget = ref(null);
const showDeleteModal = ref(false)


const loadCategories = async () => {
  try {
    const response = await fetchCategory();
    categories.value = response.data.data.map(category => ({
      categoryId: category.categoryId, // ✅ 추가!
      title: category.categoryName,
      color: category.color,
      completed: category.completed
    }));
  } catch (e) {
    console.error('카테고리 불러오기 실패:', e);
  }
};

const handleCreated = async (newCategory) => {
  try {
    const response = await createCategory({
      categoryName: newCategory.title,
      color: newCategory.color,
    });

    if (response.status === 201) {
      categories.value.push({
        title: newCategory.title,
        color: newCategory.color,
        completed: false,
      });
    }
    showModal.value = false;
  } catch (error) {
    console.error('카테고리 생성 오류:', error);
    alert('카테고리 추가에 실패했습니다.');
  }
};

const openCategoryActionModal = (category) => {
  selectedCategory.value = category;
  showActionModal.value = true;
};

const handleComplete = async (category) => {
  try {
    await completeCategory(category.categoryId);
    await loadCategories();
    showActionModal.value = false;
  } catch (e) {
    console.error('카테고리 완료 처리 실패:', e);
  }
};

const handleCancelComplete = async (category) => {
  try {
    await completeCategory(category.categoryId); // 완료 취소도 같은 API 사용
    await loadCategories();
    showActionModal.value = false;
  } catch (e) {
    console.error('카테고리 완료 취소 실패:', e);
  }
}

const handleEdit = (category) => {
  isEdit.value = true;
  editTarget.value = category;
  showActionModal.value = false;
  showModal.value = true;
};

const handleDeleteClick = (category) => {
  selectedCategory.value = category
  showDeleteModal.value = true
}

const confirmDeleteCategory = async () => {
  try {
    const hasSchedules = selectedCategory.value.tasks?.length > 0
    await deleteCategory(selectedCategory.value.categoryId, "카테고리를 삭제하겠습니다", hasSchedules)
    showDeleteModal.value = false
    selectedCategory.value = null

    window.location.reload()

    await loadCategories() // 카테고리 목록 갱신 함수
  } catch (e) {
  }
}

const handleModalClose = () => {
  showModal.value = false;
  isEdit.value = false;
  editTarget.value = null;
}
onMounted(loadCategories);
</script>

<style scoped>
.category-main {
  max-width: 1000px;
  margin: 0 auto;
  padding: 10rem;
  background-color: #F8F7FF;
}

.category-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}

h2 {
  color: #9381FF;
}

button {
  background-color: #9381FF;
  color: white;
  padding: 0.5rem 1.2rem;
  border: none;
  border-radius: 8px;
  font-size: 1rem;
  cursor: pointer;
}

.category-list {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  gap: 1.2rem;
}

.category-card {
  background-color: white;
  border-radius: 12px;
  padding: 1rem;
  box-shadow: 0 4px 10px rgba(0,0,0,0.08);
}

.card-header {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 1.2rem;
  font-weight: 600;
}

.card-footer {
  margin-top: 1rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 0.9rem;
  color: #666;
}

.tag {
  display: inline-block;
  width: 20px;
  height: 20px;
  border-radius: 50%;
}

.empty {
  text-align: center;
  color: #aaa;
  font-size: 1rem;
  margin-top: 2rem;
}
</style>
