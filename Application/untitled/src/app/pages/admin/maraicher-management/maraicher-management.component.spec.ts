import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MaraicherManagementComponent } from './maraicher-management.component';

describe('MaraicherManagementComponent', () => {
  let component: MaraicherManagementComponent;
  let fixture: ComponentFixture<MaraicherManagementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MaraicherManagementComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MaraicherManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
