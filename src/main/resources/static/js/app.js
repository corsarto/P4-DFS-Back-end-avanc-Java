const list = document.getElementById('project-list');
const form = document.getElementById('project-form');
const STATUS_LABELS = {
  PLANNED: 'Planifié',
  IN_PROGRESS: 'En cours',
  COMPLETED: 'Terminé'
};

async function loadProjects() {
  try {
    const response = await fetch('/api/projects');

    if (!response.ok) {
      throw new Error('HTTP ' + response.status);
    }

    const projects = await response.json();
    list.innerHTML = '';

    if (!projects.length) {
      list.innerHTML = '<p class="muted">Aucun projet pour le moment.</p>';
      return;
    }

    projects.forEach((project) => {
      const item = document.createElement('article');
      item.className = 'project-item';

      const title = document.createElement('h3');
      title.textContent = project.name;
      item.appendChild(title);

      const owner = document.createElement('p');
      owner.textContent = 'Client : ' + project.owner;
      owner.className = 'muted';
      item.appendChild(owner);

      const budget = document.createElement('p');
      budget.textContent = 'Budget : ' + project.budget + ' €';
      item.appendChild(budget);

      const description = document.createElement('p');
      description.textContent = 'Description : ' + (project.description || 'À compléter');
      item.appendChild(description);

      const status = document.createElement('p');
      status.textContent = 'Statut : ' + (STATUS_LABELS[project.status] || project.status || STATUS_LABELS.PLANNED);
      item.appendChild(status);
      list.appendChild(item);
    });
  } catch (error) {
    console.error(error);
    list.innerHTML = '<p class="muted">Impossible de charger les projets. Réessayez plus tard.</p>';
  }
}

form.addEventListener('submit', async (event) => {
  event.preventDefault();
  form.querySelectorAll('.msg-error').forEach((p) => { p.textContent = ''; });
  form.querySelectorAll('[aria-invalid]').forEach((input) => {
    input.removeAttribute('aria-invalid');
  });
  const payload = Object.fromEntries(new FormData(form).entries());
  payload.budget = (payload.budget === '') ? null : Number(payload.budget);

  try {
    const response = await fetch('/api/projects', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    });

    if (response.ok) {
      form.reset();
      loadProjects();
    } else {
      const errors = await response.json();
      for (const [field, message] of Object.entries(errors)) {
        const errorElement = document.getElementById(field + '-error');
        if (errorElement) {
          errorElement.textContent = message;
          document.getElementById(field).setAttribute('aria-invalid', 'true');
        }
      }
      const firstInvalid = form.querySelector('[aria-invalid="true"]');
      if (firstInvalid) {
        firstInvalid.focus();
      } else {
        document.getElementById('form-error').textContent = "L'enregistrement a échoué. Vérifiez les informations saisies et réessayez.";
      }
    }
  } catch (error) {
    console.error(error);
    document.getElementById('form-error').textContent = 'Serveur injoignable. Réessayez dans quelques instants.';
  }
});

loadProjects();