// Utility functions for API calls and state management

const API_BASE = '/api';

// Error Handler
function handleError(response) {
    if (!response.ok) {
        if(response.status === 401) {
            window.location.href = '/index.html';
        }
        return response.json().then(err => { throw new Error(err.message || 'An error occurred'); });
    }
    return response.json();
}

// Show alert message
function showAlert(message, type = 'error') {
    const alertEl = document.getElementById('alertMessage');
    if (!alertEl) return;
    
    alertEl.textContent = message;
    alertEl.className = `alert alert-${type}`;
    alertEl.style.display = 'block';
    
    setTimeout(() => {
        alertEl.style.display = 'none';
    }, 5000);
}

// Check auth state
async function checkAuth() {
    try {
        const response = await fetch(`${API_BASE}/candidates/me`);
        if (response.ok) {
            const user = await response.json();
            localStorage.setItem('candidateId', user.id);
            localStorage.setItem('candidateName', user.name);
            updateUIForUser(user);
            return user;
        } else {
            window.location.href = '/index.html';
        }
    } catch (error) {
        window.location.href = '/index.html';
    }
}

function updateUIForUser(user) {
    const nameEls = document.querySelectorAll('.user-name-display');
    nameEls.forEach(el => el.textContent = user.name);
}

// Logout
async function logout() {
    try {
        await fetch(`${API_BASE}/candidates/logout`, { method: 'POST' });
        localStorage.clear();
        window.location.href = '/index.html';
    } catch (error) {
        console.error('Logout failed', error);
    }
}

// Initialize sidebar navigation highlighting
document.addEventListener('DOMContentLoaded', () => {
    const currentPath = window.location.pathname;
    const navLinks = document.querySelectorAll('.sidebar-menu a');
    navLinks.forEach(link => {
        if (link.getAttribute('href') === currentPath.substring(1)) {
            link.classList.add('active');
        }
    });

    const logoutBtn = document.getElementById('logoutBtn');
    if(logoutBtn) {
        logoutBtn.addEventListener('click', (e) => {
            e.preventDefault();
            logout();
        });
    }
});
