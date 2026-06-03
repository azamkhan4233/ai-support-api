INSERT INTO businesses (name, description, industry, contact_email, agent_system_prompt, created_at, active, public_api_key)
VALUES ('TechCorp Solutions', 'Leading technology solutions provider', 'Technology',
        'contact@techcorp.com',
        'You are a helpful customer support assistant for TechCorp Solutions. Be professional, friendly, and provide accurate information about our products and services. If you don''t know something, offer to connect the customer with a human representative.',
        NOW(), true, 'demo-api-key-techcorp');

INSERT INTO users (email, name, password, role, business_id, created_at, active)
VALUES ('admin@techcorp.com', 'Admin User',
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWq',
        'ADMIN', 1, NOW(), true);