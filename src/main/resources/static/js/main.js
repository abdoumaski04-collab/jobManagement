document.addEventListener('DOMContentLoaded', () => {
  // Auto-dismiss or manual dismiss for alerts
  document.querySelectorAll('.alert-close').forEach(button => {
    button.addEventListener('click', () => {
      const alert = button.closest('.alert');
      if (alert) {
        alert.style.opacity = '0';
        alert.style.transform = 'translateY(-8px)';
        setTimeout(() => alert.remove(), 250);
      }
    });
  });

  // Modal handlers
  window.openModal = function(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) {
      modal.classList.add('active');
    }
  };

  window.closeModal = function(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) {
      modal.classList.remove('active');
    }
  };

  // Close modal when clicking backdrop
  document.querySelectorAll('.modal-backdrop').forEach(modal => {
    modal.addEventListener('click', (e) => {
      if (e.target === modal) {
        modal.classList.remove('active');
      }
    });
  });

  // Custom File Input Preview
  const fileInputs = document.querySelectorAll('input[type="file"]');
  fileInputs.forEach(input => {
    input.addEventListener('change', (e) => {
      const fileNameTarget = document.getElementById(input.dataset.previewTarget);
      if (fileNameTarget && input.files.length > 0) {
        fileNameTarget.textContent = input.files[0].name;
      }
    });
  });
});
