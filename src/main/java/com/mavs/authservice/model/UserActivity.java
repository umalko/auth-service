package com.mavs.authservice.model;

import com.mavs.activity.model.Activity;
import com.mavs.activity.model.ActivityProcessType;
import com.mavs.activity.model.ActivityType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@Table(name = "activities")
public class UserActivity extends Activity {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Override
    public String getId() {
        return super.getId();
    }


    @Enumerated(EnumType.STRING)
    @Override
    public ActivityType getType() {
        return super.getType();
    }

    @Enumerated(EnumType.STRING)
    @Override
    public ActivityProcessType getProcessType() {
        return super.getProcessType();
    }

    @Lob
    @Override
    public String getJsonObject() {
        return super.getJsonObject();
    }

    public UserActivity(Activity activity) {
        super(activity);
    }
}
