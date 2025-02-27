INSERT INTO SponsorOrganizations (name, type, invoice_address, nif_vat) VALUES
('TechCorp', 'Private corp.', '123 Tech Street', 'A12345678'),
('GreenGov', 'Public administration', '456 Green Avenue', 'B23456789'),
('EduFund', 'Private corp.', '789 Edu Lane', 'C34567890'),
('HealthPlus', 'Public administration', '101 Health Blvd', 'D45678901');

INSERT INTO GBMembers (name, role) VALUES
('Alice Johnson', 'President'),
('Bob Smith', 'Treasurer'),
('Charlie Davis', 'Secretary'),
('Diana White', 'Member');

INSERT INTO SponsorContact (sponsor_org_id, name, email, phone) VALUES
(1, 'John Doe', 'john.doe@techcorp.com', '123-456-7890'),
(2, 'Sarah Green', 'sarah.green@greengov.org', '234-567-8901'),
(3, 'Mark Blue', 'mark.blue@edufund.com', '345-678-9012'),
(4, 'Emma Red', 'emma.red@healthplus.org', '456-789-0123');

INSERT INTO SponsorshipAgreements (sponsor_org_id, amount, date_agreement, status) VALUES
(1, 5000.00, '2024-01-10', 'estimated'),
(2, 10000.00, '2024-02-15', 'paid'),
(3, 7500.00, '2024-03-20', 'cancelled'),
(4, 12000.00, '2024-04-25', 'paid');

INSERT INTO Invoices (sponsorship_agreement_id, date_issue, date_send) VALUES
(1, '2024-01-11', '2024-01-12'),
(2, '2024-02-16', '2024-02-17'),
(3, '2024-03-21', '2024-03-22'),
(4, '2024-04-26', '2024-04-27');

INSERT INTO SponsorshipPayments (invoice_id, date_received, amount) VALUES
(1, '2024-01-20', 5000.00),
(2, '2024-02-25', 10000.00),
(3, '2024-03-30', 7500.00),
(4, '2024-04-30', 12000.00);

INSERT INTO Activities (name, state, date_celebration, place_celebration, estimated_income, estimated_expenses) VALUES
('Tech Expo', 'Planned', '2024-05-10', 'Tech Park', 20000.00, 15000.00),
('Green Initiative', 'Ongoing', '2024-06-15', 'City Hall', 5000.00, 3000.00),
('Education Summit', 'Completed', '2024-07-20', 'University Center', 10000.00, 7000.00),
('Health Fair', 'Planned', '2024-08-25', 'Community Center', 8000.00, 5000.00);

INSERT INTO Levels (name, fee) VALUES
('Bronze', 1000.00),
('Silver', 2500.00),
('Gold', 5000.00),
('Platinum', 10000.00);

INSERT INTO ActivityLevel (activity_id, level_id) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4);

INSERT INTO IncomesExpenses (concept, amount, date_movement, status) VALUES
('Ticket Sales', 15000.00, '2024-05-11', 'paid'),
('Sponsorship Contribution', 5000.00, '2024-06-16', 'unpaid'),
('Merchandise Sales', 3000.00, '2024-07-21', 'paid'),
('Donations', 4000.00, '2024-08-26', 'unpaid');
